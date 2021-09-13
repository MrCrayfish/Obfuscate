package com.mrcrayfish.obfuscate;

import com.mrcrayfish.obfuscate.client.ClientHandler;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.mrcrayfish.obfuscate.network.PacketHandler;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupClient);
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupCapabilities);
    }

    private void setupCommon(FMLCommonSetupEvent event)
    {
        PacketHandler.register();
    }

    private void setupClient(FMLClientSetupEvent event)
    {
        ClientHandler.instance().setup();
    }

    public void setupCapabilities(RegisterCapabilitiesEvent event)
    {
        SyncedPlayerData.onRegisterCapability(event);
    }
}
