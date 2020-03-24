package com.mrcrayfish.obfuscate.common.data;

import com.google.common.collect.ImmutableList;
import com.mrcrayfish.obfuscate.Reference;
import com.mrcrayfish.obfuscate.network.HandshakeMessages;
import com.mrcrayfish.obfuscate.network.PacketHandler;
import com.mrcrayfish.obfuscate.network.message.MessageSyncPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Basically a clone of DataParameter system. It's not good to init custom data parameters to
 * other entities that aren't your own. It can cause mismatched ids and crash the game. This synced
 * data system attempts to solve the problem (at least for player entities) and allows data to be
 * easily synced to clients. The data can only be controlled on the logical server. Changing the
 * data on the logical client will have no affect on the server.</p>
 * <p></p>
 * <p>To use this system you first need to create a synced data key instance. This should be a public
 * static final field. You will need to specify an key id (based on your modid), the serializer, and
 * a default value supplier.</p>
 * <code>public static final SyncedDataKey&lt;Double&gt; CURRENT_SPEED = SyncedDataKey.create(new ResourceLocation("examplemod:speed"), Serializers.DOUBLE, () -> 0.0);</code>
 * <p></p>
 * <p>Next the key needs to be registered. This can simply be done in the common setup of your mod.</p>
 * <code>SyncedPlayerData.instance().registerKey(CURRENT_SPEED);</code>
 * <p></p>
 * <p>Then anywhere you want (as long as it's on the main thread), you can set the value by calling</p>
 * <code>SyncedPlayerData.instance().set(player, CURRENT_SPEED, 5.0);</code>
 * <p></p>
 * <p>The value can be retrieved on the server or client by calling</p>
 * <code>SyncedPlayerData.instance().get(player, CURRENT_SPEED);</code>
 * <p></p>
 * <p>Author: MrCrayfish</p>
 */
public class SyncedPlayerData
{
    @CapabilityInject(DataHolder.class)
    public static final Capability<DataHolder> CAPABILITY = null;

    private static SyncedPlayerData instance;
    private static boolean init = false;

    private final Map<ResourceLocation, SyncedDataKey<?>> registeredDataKeys = new HashMap<>();
    private final Map<Integer, SyncedDataKey<?>> idToDataKey = new HashMap<>();
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

    public static void init()
    {
        if(!init)
        {
            CapabilityManager.INSTANCE.register(DataHolder.class, new Storage(), DataHolder::new);
            MinecraftForge.EVENT_BUS.register(instance());
            init = true;
        }
    }

    /**
     * Registers a synced data key into the system.
     *
     * @param key a synced data key instance
     */
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

    /**
     * Sets the value of a synced data key to the specified player
     *
     * @param player the player to assign the value to
     * @param key    a registered synced data key
     * @param value  a new value that matches the synced data key type
     */
    public <T> void set(PlayerEntity player, SyncedDataKey<T> key, T value)
    {
        if(!this.registeredDataKeys.values().contains(key))
        {
            throw new IllegalArgumentException(String.format("The data key '%s' is not registered!", key.getKey()));
        }
        DataHolder holder = this.getDataHolder(player);
        if(holder != null && holder.set(player, key, value))
        {
            if(!player.world.isRemote)
            {
                this.dirty = true;
            }
        }
    }

    /**
     * Gets the value for the synced data key from the specified player. It is best to check that
     * the player is alive before getting the value.
     *
     * @param player the player to retrieve the data from
     * @param key    a registered synced data key
     */
    public <T> T get(PlayerEntity player, SyncedDataKey<T> key)
    {
        if(!this.registeredDataKeys.values().contains(key))
        {
            throw new IllegalArgumentException(String.format("The data key '%s' is not registered!", key.getKey()));
        }
        DataHolder holder = this.getDataHolder(player);
        return holder != null ? holder.get(key) : key.getDefaultValueSupplier().get();
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

    @Nullable
    private DataHolder getDataHolder(PlayerEntity player)
    {
        return player.getCapability(CAPABILITY, null).orElse(null);
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "synced_player_data"), new Provider());
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event)
    {
        if(event.getTarget() instanceof PlayerEntity && !event.getPlayer().world.isRemote)
        {
            PlayerEntity player = (PlayerEntity) event.getTarget();
            DataHolder holder = this.getDataHolder(player);
            if(holder != null)
            {
                List<SyncedPlayerData.DataEntry<?>> entries = holder.gatherAll();
                entries.removeIf(entry -> !entry.getKey().shouldSyncToAllPlayers());
                if(!entries.isEmpty())
                {
                    PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new MessageSyncPlayerData(player.getEntityId(), entries));
                }
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
            DataHolder holder = this.getDataHolder(player);
            if(holder != null)
            {
                List<SyncedPlayerData.DataEntry<?>> entries = holder.gatherAll();
                if(!entries.isEmpty())
                {
                    PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new MessageSyncPlayerData(player.getEntityId(), entries));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        PlayerEntity original = event.getOriginal();
        if(!original.world.isRemote)
        {
            PlayerEntity player = event.getPlayer();
            DataHolder oldHolder = this.getDataHolder(original);
            if(oldHolder != null)
            {
                DataHolder newHolder = this.getDataHolder(player);
                if(newHolder != null)
                {
                    Map<SyncedDataKey<?>, DataEntry<?>> dataMap = new HashMap<>(oldHolder.dataMap);
                    if(event.isWasDeath())
                    {
                        dataMap.entrySet().removeIf(entry -> !entry.getKey().isPersistent());
                    }
                    newHolder.dataMap = dataMap;
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            if(this.dirty)
            {
                PlayerEntity player = event.player;
                if(!player.world.isRemote())
                {
                    DataHolder holder = this.getDataHolder(player);
                    if(holder != null && holder.isDirty())
                    {
                        List<SyncedPlayerData.DataEntry<?>> entries = holder.gatherDirty();
                        if(!entries.isEmpty())
                        {
                            PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new MessageSyncPlayerData(player.getEntityId(), entries));
                            List<SyncedPlayerData.DataEntry<?>> syncToAllEntries = entries.stream().filter(entry -> entry.getKey().shouldSyncToAllPlayers()).collect(Collectors.toList());
                            if(!syncToAllEntries.isEmpty())
                            {
                                PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageSyncPlayerData(player.getEntityId(), syncToAllEntries));
                            }
                        }
                        holder.clean();
                    }
                }
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
                this.dirty = false;
            }
        }
    }

    public static class DataHolder
    {
        private Map<SyncedDataKey<?>, DataEntry<?>> dataMap = new HashMap<>();
        private boolean dirty = false;

        @SuppressWarnings("unchecked")
        public <T> boolean set(PlayerEntity player, SyncedDataKey<T> key, T value)
        {
            DataEntry<T> entry = (DataEntry<T>) this.dataMap.computeIfAbsent(key, DataEntry::new);
            if(!entry.getValue().equals(value))
            {
                boolean dirty = !player.world.isRemote && entry.getKey().shouldSyncToClient();
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
            return (T) this.dataMap.computeIfAbsent(key, DataEntry::new).getValue();
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
            return this.dataMap.values().stream().filter(DataEntry::isDirty).filter(entry -> entry.getKey().shouldSyncToClient()).collect(Collectors.toList());
        }

        public List<DataEntry<?>> gatherAll()
        {
            return this.dataMap.values().stream().filter(entry -> entry.getKey().shouldSyncToClient()).collect(Collectors.toList());
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

        private INBT writeValue()
        {
            return this.key.getSerializer().write(this.value);
        }

        private void readValue(INBT nbt)
        {
            this.value = this.key.getSerializer().read(nbt);
        }
    }

    public boolean updateMappings(HandshakeMessages.S2CSyncedPlayerData message)
    {
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

    public static class Storage implements Capability.IStorage<DataHolder>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<DataHolder> capability, DataHolder instance, Direction side)
        {
            ListNBT list = new ListNBT();
            instance.dataMap.forEach((key, entry) ->
            {
                if(key.shouldSave())
                {
                    CompoundNBT keyTag = new CompoundNBT();
                    keyTag.putString("Key", key.getKey().toString());
                    keyTag.put("Value", entry.writeValue());
                    list.add(keyTag);
                }
            });
            return list;
        }

        @Override
        public void readNBT(Capability<DataHolder> capability, DataHolder instance, Direction side, INBT nbt)
        {
            ListNBT list = (ListNBT) nbt;
            list.forEach(entryTag ->
            {
                CompoundNBT keyTag = (CompoundNBT) entryTag;
                ResourceLocation key = ResourceLocation.tryCreate(keyTag.getString("Key"));
                INBT value = keyTag.get("Value");
                SyncedDataKey<?> syncedDataKey = SyncedPlayerData.instance().registeredDataKeys.get(key);
                if(syncedDataKey != null && syncedDataKey.shouldSave())
                {
                    DataEntry<?> entry = new DataEntry<>(syncedDataKey);
                    entry.readValue(value);
                    instance.dataMap.put(syncedDataKey, entry);
                }
            });
        }
    }

    public static class Provider implements ICapabilitySerializable<ListNBT>
    {
        final DataHolder INSTANCE = CAPABILITY.getDefaultInstance();

        @Override
        public ListNBT serializeNBT()
        {
            return (ListNBT) CAPABILITY.getStorage().writeNBT(CAPABILITY, INSTANCE, null);
        }

        @Override
        public void deserializeNBT(ListNBT compound)
        {
            CAPABILITY.getStorage().readNBT(CAPABILITY, INSTANCE, null, compound);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return CAPABILITY.orEmpty(cap, LazyOptional.of(() -> INSTANCE));
        }
    }
}