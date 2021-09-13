package com.mrcrayfish.obfuscate.network;

import com.mrcrayfish.obfuscate.Reference;
import com.mrcrayfish.obfuscate.network.message.IMessage;
import com.mrcrayfish.obfuscate.network.message.MessageSyncPlayerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.FMLHandshakeHandler;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

/**
 * Author: MrCrayfish
 */
public class PacketHandler
{
    private static final String PROTOCOL_VERSION = "OBFUSCATE_V1";
    private static final SimpleChannel HANDSHAKE_CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, "handshake"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    private static final SimpleChannel PLAY_CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, "play"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    private static int nextId;

    public static void register()
    {
        HANDSHAKE_CHANNEL.messageBuilder(HandshakeMessages.C2SAcknowledge.class, 99)
            .loginIndex(HandshakeMessages.LoginIndexedMessage::getLoginIndex, HandshakeMessages.LoginIndexedMessage::setLoginIndex)
            .decoder(HandshakeMessages.C2SAcknowledge::decode)
            .encoder(HandshakeMessages.C2SAcknowledge::encode)
            .consumer(FMLHandshakeHandler.indexFirst((handler, msg, s) -> HandshakeHandler.handleAcknowledge(msg, s)))
            .add();

        HANDSHAKE_CHANNEL.messageBuilder(HandshakeMessages.S2CSyncedPlayerData.class, 1)
            .loginIndex(HandshakeMessages.LoginIndexedMessage::getLoginIndex, HandshakeMessages.LoginIndexedMessage::setLoginIndex)
            .decoder(HandshakeMessages.S2CSyncedPlayerData::decode)
            .encoder(HandshakeMessages.S2CSyncedPlayerData::encode)
            .consumer(FMLHandshakeHandler.biConsumerFor((handler, msg, supplier) -> HandshakeHandler.handleSyncedPlayerData(msg, supplier)))
            .markAsLoginPacket()
            .add();

        registerPlayMessage(MessageSyncPlayerData.class, new MessageSyncPlayerData());
    }

    private static <T> void registerPlayMessage(Class<T> clazz, IMessage<T> message)
    {
        PLAY_CHANNEL.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }

    static SimpleChannel getHandshakeChannel()
    {
        return HANDSHAKE_CHANNEL;
    }

    public static SimpleChannel getPlayChannel()
    {
        return PLAY_CHANNEL;
    }
}
