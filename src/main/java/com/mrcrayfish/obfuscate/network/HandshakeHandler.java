package com.mrcrayfish.obfuscate.network;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
class HandshakeHandler
{
    private static final Marker OBFUSCATE_HANDSHAKE = MarkerManager.getMarker("OBFUSCATE_HANDSHAKE");

    static void handleAcknowledge(HandshakeMessages.C2SAcknowledge message, Supplier<NetworkEvent.Context> c)
    {
        Obfuscate.LOGGER.debug(OBFUSCATE_HANDSHAKE, "Received acknowledgement from client");
        c.get().setPacketHandled(true);
    }

    static void handleSyncedPlayerData(HandshakeMessages.S2CSyncedPlayerData message, Supplier<NetworkEvent.Context> c)
    {
        Obfuscate.LOGGER.debug(OBFUSCATE_HANDSHAKE, "Received synced key mappings from server");
        c.get().setPacketHandled(true);
        if(!SyncedPlayerData.instance().updateMappings(message))
        {
            c.get().getNetworkManager().disconnect(new StringTextComponent("Connection closed - [Obfuscate] received unknown synced data key"));
            return;
        }
        PacketHandler.getHandshakeChannel().reply(new HandshakeMessages.C2SAcknowledge(), c.get());
    }
}
