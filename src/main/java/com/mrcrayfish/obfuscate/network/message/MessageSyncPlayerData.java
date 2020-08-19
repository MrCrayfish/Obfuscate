package com.mrcrayfish.obfuscate.network.message;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.client.ClientHandler;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageSyncPlayerData implements IMessage<MessageSyncPlayerData>
{
    private int entityId;
    private List<SyncedPlayerData.DataEntry<?>> entries;

    public MessageSyncPlayerData() {}

    public MessageSyncPlayerData(int entityId, List<SyncedPlayerData.DataEntry<?>> entries)
    {
        this.entityId = entityId;
        this.entries = entries;
    }

    @Override
    public void encode(MessageSyncPlayerData message, PacketBuffer buffer)
    {
        buffer.writeVarInt(message.entityId);
        buffer.writeVarInt(message.entries.size());
        message.entries.forEach(entry -> entry.write(buffer));
    }

    @Override
    public MessageSyncPlayerData decode(PacketBuffer buffer)
    {
        int entityId = buffer.readVarInt();
        int size = buffer.readVarInt();
        List<SyncedPlayerData.DataEntry<?>> entries = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            entries.add(SyncedPlayerData.DataEntry.read(buffer));
        }
        return new MessageSyncPlayerData(entityId, entries);
    }

    @Override
    public void handle(MessageSyncPlayerData message, Supplier<NetworkEvent.Context> supplier)
    {
        if(supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            supplier.get().enqueueWork(() -> ClientHandler.instance().updatePlayerData(message.entityId, message.entries));
            supplier.get().setPacketHandled(true);
        }
    }
}
