package com.mrcrayfish.obfuscate;

import com.mrcrayfish.obfuscate.client.ClientHandler;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.mrcrayfish.obfuscate.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: MrCrayfish
 */
@Mod(Reference.MOD_ID)
public class Obfuscate
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    public Obfuscate()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setupCommon(FMLCommonSetupEvent event)
    {
        SyncedPlayerData.init();
        PacketHandler.register();
    }

    private void setupClient(FMLClientSetupEvent event)
    {
        ClientHandler.instance().setup();
    }
}
