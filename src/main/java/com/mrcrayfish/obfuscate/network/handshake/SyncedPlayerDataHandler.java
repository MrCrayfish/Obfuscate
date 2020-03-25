package com.mrcrayfish.obfuscate.network.handshake;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Author: MrCrayfish
 */
public class SyncedPlayerDataHandler extends SimpleChannelInboundHandler<ObfuscateMessage.SyncedPlayerDataMessage>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ObfuscateMessage.SyncedPlayerDataMessage msg) throws Exception
    {
        SyncedPlayerData.instance().updateMappings(msg);
    }
}
