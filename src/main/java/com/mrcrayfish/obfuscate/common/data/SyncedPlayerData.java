package com.mrcrayfish.obfuscate.common.data;

import com.google.common.collect.ImmutableList;
import com.mrcrayfish.obfuscate.network.HandshakeMessages;
import com.mrcrayfish.obfuscate.network.PacketHandler;
import com.mrcrayfish.obfuscate.network.message.MessageSyncPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Basically a clone of DataParameter system. It's not good to register custom data parameters to
 * other entities that aren't your own. It can cause mismatched ids and crash the game. This synced
 * data system attempts to solve the problem (at least for players) and allows data to be synced to
 * clients. The data can only be controlled on the server. Changing the data on the client will have
 * no affect on the server.
 *
 * Author: MrCrayfish
 */
public class SyncedPlayerData
{
    private static SyncedPlayerData instance;

    private final Map<ResourceLocation, SyncedDataKey<?>> registeredDataKeys = new HashMap<>();
    private final Map<Integer, SyncedDataKey<?>> idToDataKey = new HashMap<>();
    private final WeakHashMap<PlayerEntity, Holder> playerDataMap = new WeakHashMap<>();
    private int nextKeyId = 0;
    private boolean dirty = false;

    private SyncedPlayerData() {}

    public static SyncedPlayerData instance()
    {
        if(instance == null)
        {
            instance = new SyncedPlayerData();
        }
        return instance;
    }
    
    public void registerKey(SyncedDataKey<?> key)
    {
        if(this.registeredDataKeys.containsKey(key.getKey()))
        {
            throw new IllegalArgumentException(String.format("The data key '%s' is already registered!", key.getKey()));
        }
        int nextId = this.nextKeyId++;
        key.setId(nextId);
        this.registeredDataKeys.put(key.getKey(), key);
        this.idToDataKey.put(nextId, key);
    }

    public <T> void set(PlayerEntity player, SyncedDataKey<T> key, T value)
    {
        if(!this.registeredDataKeys.values().contains(key))
        {
            throw new IllegalArgumentException(String.format("The data key '%s' is not registered!", key.getKey()));
        }
        Holder holder = this.getPlayerData(player);
        if(holder.set(key, value))
        {
            if(!player.world.isRemote)
            {
                this.dirty = true;
            }
        }
    }

    public <T> T get(PlayerEntity player, SyncedDataKey<T> key)
    {
        if(!this.registeredDataKeys.values().contains(key))
        {
            throw new IllegalArgumentException(String.format("The data key '%s' is not registered!", key.getKey()));
        }
        Holder holder = this.getPlayerData(player);
        return holder.get(key);
    }

    @Nullable
    public SyncedDataKey<?> getKey(int id)
    {
        return this.idToDataKey.get(id);
    }

    public List<SyncedDataKey<?>> getKeys()
    {
        return ImmutableList.copyOf(this.registeredDataKeys.values());
    }

    private Holder getPlayerData(PlayerEntity player)
    {
        return this.playerDataMap.computeIfAbsent(player, Holder::new);
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event)
    {
        if(event.getTarget() instanceof PlayerEntity && !event.getPlayer().world.isRemote)
        {
            PlayerEntity player = (PlayerEntity) event.getTarget();
            Holder holder = this.getPlayerData(player);
            if(holder != null)
            {
                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new MessageSyncPlayerData(player.getEntityId(), holder, true));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if(entity instanceof PlayerEntity && !event.getWorld().isRemote)
        {
            PlayerEntity player = (PlayerEntity) entity;
            Holder holder = this.getPlayerData(player);
            if(holder != null)
            {
                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new MessageSyncPlayerData(player.getEntityId(), holder, true));
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            if(this.dirty)
            {
                this.playerDataMap.forEach((player, holder) ->
                {
                    if(player.isAlive() && holder.isDirty())
                    {
                        PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new MessageSyncPlayerData(player.getEntityId(), holder, false));
                        holder.clean();
                    }
                });
                this.dirty = false;
            }
        }
    }

    public static class Holder
    {
        private WeakReference<PlayerEntity> playerRef;
        private final Map<SyncedDataKey<?>, DataEntry<?>> dataMap = new HashMap<>();
        private boolean dirty = false;

        Holder(PlayerEntity player)
        {
            this.playerRef = new WeakReference<>(player);
        }

        @SuppressWarnings("unchecked")
        public <T> boolean set(SyncedDataKey<T> key, T value)
        {
            DataEntry<T> entry = (DataEntry<T>) this.dataMap.computeIfAbsent(key, DataEntry::new);
            if(!entry.getValue().equals(value))
            {
                PlayerEntity player = this.playerRef.get();
                boolean dirty = player != null && !player.world.isRemote;
                entry.setValue(value, dirty);
                this.dirty = dirty;
                return true;
            }
            return false;
        }

        @Nullable
        @SuppressWarnings("unchecked")
        public <T> T get(SyncedDataKey<T> key)
        {
            return (T) this.dataMap.computeIfAbsent(key, DataEntry::new);
        }

        public boolean isDirty()
        {
            return this.dirty;
        }

        public void clean()
        {
            this.dirty = false;
            this.dataMap.forEach((key, entry) -> entry.clean());
        }

        public List<DataEntry<?>> gatherDirty()
        {
            return this.dataMap.values().stream().filter(DataEntry::isDirty).collect(Collectors.toList());
        }

        public List<DataEntry<?>> gatherAll()
        {
            return new ArrayList<>(this.dataMap.values());
        }
    }

    public static class DataEntry<T>
    {
        private SyncedDataKey<T> key;
        private T value;
        private boolean dirty;

        private DataEntry(SyncedDataKey<T> key)
        {
            this.key = key;
            this.value = key.getDefaultValueSupplier().get();
        }

        public SyncedDataKey<T> getKey()
        {
            return this.key;
        }

        public T getValue()
        {
            return this.value;
        }

        public void setValue(T value, boolean dirty)
        {
            this.value = value;
            this.dirty = dirty;
        }

        public boolean isDirty()
        {
            return this.dirty;
        }

        public void clean()
        {
            this.dirty = false;
        }

        public void write(PacketBuffer buffer)
        {
            buffer.writeVarInt(this.key.getId());
            this.key.getSerializer().write(buffer, this.value);
        }

        public static DataEntry<?> read(PacketBuffer buffer)
        {
            SyncedDataKey<?> key = SyncedPlayerData.instance().getKey(buffer.readVarInt());
            Validate.notNull(key, "Synced key does not exist for id");
            DataEntry<?> entry = new DataEntry<>(key);
            entry.readValue(buffer);
            return entry;
        }

        private void readValue(PacketBuffer buffer)
        {
            this.value = this.getKey().getSerializer().read(buffer);
        }
    }

    public boolean updateMappings(HandshakeMessages.S2CSyncedPlayerData message)
    {
        this.playerDataMap.clear();
        this.idToDataKey.clear();
        Map<ResourceLocation, Integer> keyMappings = message.getKeyMap();
        for(ResourceLocation key : keyMappings.keySet())
        {
            SyncedDataKey<?> syncedDataKey = this.registeredDataKeys.get(key);
            if(syncedDataKey == null) return false;
            int id = keyMappings.get(key);
            syncedDataKey.setId(id);
            this.idToDataKey.put(id, syncedDataKey);
        }
        return true;
    }
}