package com.mrcrayfish.obfuscate.network.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;

/**
 * Author: MrCrayfish
 */
public class ObfuscateRuntimeCodec extends FMLIndexedMessageToMessageCodec<ObfuscateMessage>
{
    public ObfuscateRuntimeCodec()
    {
        this.addDiscriminator(0, ObfuscateMessage.SyncedPlayerDataMessage.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ObfuscateMessage msg, ByteBuf target) throws Exception
    {
        msg.toBytes(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, ObfuscateMessage msg)
    {
        msg.fromBytes(source);
    }
}
