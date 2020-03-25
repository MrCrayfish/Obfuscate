package com.mrcrayfish.obfuscate.network.handshake;

import com.mrcrayfish.obfuscate.common.data.SyncedDataKey;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public abstract class ObfuscateMessage
{
    abstract void toBytes(ByteBuf bytes);
    abstract void fromBytes(ByteBuf bytes);

    public static class SyncedPlayerDataMessage extends ObfuscateMessage
    {
        private Map<ResourceLocation, Integer> keyMap;

        public SyncedPlayerDataMessage()
        {
            this.keyMap = new HashMap<>();
            List<SyncedDataKey<?>> keys = SyncedPlayerData.instance().getKeys();
            keys.forEach(syncedDataKey -> this.keyMap.put(syncedDataKey.getKey(), syncedDataKey.getId()));
        }

        @Override
        void toBytes(ByteBuf bytes)
        {
            bytes.writeInt(this.keyMap.size());
            this.keyMap.forEach((key, id) -> {
                ByteBufUtils.writeUTF8String(bytes, key.toString());
                ByteBufUtils.writeVarInt(bytes, id, 2);
            });
        }

        @Override
        void fromBytes(ByteBuf bytes)
        {
            this.keyMap = new HashMap<>();
            int size = bytes.readInt();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation key = new ResourceLocation(ByteBufUtils.readUTF8String(bytes));
                int id = ByteBufUtils.readVarInt(bytes, 2);
                this.keyMap.put(key, id);
            }
        }

        public Map<ResourceLocation, Integer> getKeyMap()
        {
            return this.keyMap;
        }
    }
}
