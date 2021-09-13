package com.mrcrayfish.obfuscate.network;

import com.mrcrayfish.obfuscate.common.data.SyncedDataKey;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

/**
 * Author: MrCrayfish
 */
public class HandshakeMessages
{
    static class LoginIndexedMessage implements IntSupplier
    {
        private int loginIndex;

        void setLoginIndex(final int loginIndex)
        {
            this.loginIndex = loginIndex;
        }

        int getLoginIndex()
        {
            return this.loginIndex;
        }

        @Override
        public int getAsInt()
        {
            return getLoginIndex();
        }
    }

    static class C2SAcknowledge extends LoginIndexedMessage
    {
        void encode(FriendlyByteBuf buf) {}

        static C2SAcknowledge decode(FriendlyByteBuf buf)
        {
            return new C2SAcknowledge();
        }
    }

    public static class S2CSyncedPlayerData extends LoginIndexedMessage
    {
        private Map<ResourceLocation, Integer> keyMap;

        public S2CSyncedPlayerData()
        {
            this.keyMap = new HashMap<>();
            List<SyncedDataKey<?>> keys = SyncedPlayerData.instance().getKeys();
            keys.forEach(syncedDataKey -> this.keyMap.put(syncedDataKey.getKey(), syncedDataKey.getId()));
        }

        private S2CSyncedPlayerData(Map<ResourceLocation, Integer> keyMap)
        {
            this.keyMap = keyMap;
        }

        void encode(FriendlyByteBuf output)
        {
            List<SyncedDataKey<?>> keys = SyncedPlayerData.instance().getKeys();
            keys.forEach(syncedDataKey -> {
                output.writeResourceLocation(syncedDataKey.getKey());
                output.writeVarInt(syncedDataKey.getId());
            });
        }

        static S2CSyncedPlayerData decode(FriendlyByteBuf input)
        {
            Map<ResourceLocation, Integer> keyMap = new HashMap<>();
            List<SyncedDataKey<?>> keys = SyncedPlayerData.instance().getKeys();
            keys.forEach(syncedDataKey -> keyMap.put(input.readResourceLocation(), input.readVarInt()));
            return new S2CSyncedPlayerData(keyMap);
        }

        public Map<ResourceLocation, Integer> getKeyMap()
        {
            return this.keyMap;
        }
    }
}
