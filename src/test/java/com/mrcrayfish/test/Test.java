package com.mrcrayfish.test;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Author: MrCrayfish
 */
@Mod(modid = "living_entity_init_test", name = "Living Entity Init Test", version = "1.0.0", acceptableRemoteVersions = "*", dependencies = "required-after:obfuscate@[0.1.0,)")
public class Test
{
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
