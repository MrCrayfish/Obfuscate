package com.mrcrayfish.obfuscate.network.message;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class MessageSyncPlayerData implements IMessage, IMessageHandler<MessageSyncPlayerData, IMessage>
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
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeVarInt(buf, this.entityId, 4);
        ByteBufUtils.writeVarInt(buf, this.entries.size(), 4);
        this.entries.forEach(entry -> entry.write(buf));
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = ByteBufUtils.readVarInt(buf, 4);
        int size = ByteBufUtils.readVarInt(buf, 4);
        List<SyncedPlayerData.DataEntry<?>> entries = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            entries.add(SyncedPlayerData.DataEntry.read(buf));
        }
        this.entries = entries;
    }

    @Override
    public IMessage onMessage(MessageSyncPlayerData message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
        {
            Obfuscate.proxy.updatePlayerData(message.entityId, message.entries);
        });
        return null;
    }
}
