package com.mrcrayfish.obfuscate.network.handshake;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraftforge.fml.common.network.NetworkHandshakeEstablished;

/**
 * Author: MrCrayfish
 */
public class S2CConnectionEstablishedHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof NetworkHandshakeEstablished)
        {
            ctx.writeAndFlush(new ObfuscateMessage.SyncedPlayerDataMessage());
            return;
        }
        ctx.fireUserEventTriggered(evt);
    }
}
