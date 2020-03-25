package com.mrcrayfish.obfuscate.network;

import com.mrcrayfish.obfuscate.network.handshake.ObfuscateRuntimeCodec;
import com.mrcrayfish.obfuscate.network.handshake.S2CConnectionEstablishedHandler;
import com.mrcrayfish.obfuscate.network.handshake.SyncedPlayerDataHandler;
import com.mrcrayfish.obfuscate.network.message.MessageSyncPlayerData;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

/**
 * Author: MrCrayfish
 */
public class PacketHandler
{
    private static EnumMap<Side, FMLEmbeddedChannel> handshakeChannels;
    private static SimpleNetworkWrapper playChannel;

    public static SimpleNetworkWrapper getPlayChannel()
    {
        return PacketHandler.playChannel;
    }

    public static void register(Side side)
    {
        PacketHandler.playChannel = NetworkRegistry.INSTANCE.newSimpleChannel("OBFUSCATE|PLAY");
        PacketHandler.handshakeChannels = NetworkRegistry.INSTANCE.newChannel("OBFUSCATE|HS", new ObfuscateRuntimeCodec());
        if (side == Side.CLIENT)
        {
            addClientHandlers();
        }
        FMLEmbeddedChannel serverChannel = PacketHandler.handshakeChannels.get(Side.SERVER);
        serverChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.NOWHERE);
        String handlerName = serverChannel.findChannelHandlerNameForType(ObfuscateRuntimeCodec.class);
        serverChannel.pipeline().addAfter(handlerName, "ServerToClientConnection", new S2CConnectionEstablishedHandler());
        registerPlayMessages();
    }

    @SideOnly(Side.CLIENT)
    private static void addClientHandlers()
    {
        FMLEmbeddedChannel clientChannel = PacketHandler.handshakeChannels.get(Side.CLIENT);
        String handlerName = clientChannel.findChannelHandlerNameForType(ObfuscateRuntimeCodec.class);
        clientChannel.pipeline().addAfter(handlerName, "SyncedPlayerData", new SyncedPlayerDataHandler());
    }

    private static void registerPlayMessages()
    {
        PacketHandler.playChannel.registerMessage(MessageSyncPlayerData.class, MessageSyncPlayerData.class, 0, Side.CLIENT);
    }
}
