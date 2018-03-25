package com.mrcrayfish.test;

import com.mrcrayfish.obfuscate.common.event.EntityLivingInitEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    private static final DataParameter<Boolean> AIMING = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);

    @SubscribeEvent
    public void onEntityInit(EntityLivingInitEvent event)
    {
        if(event.getEntityLiving() instanceof EntityPlayer)
        {
            event.getEntityLiving().getDataManager().register(AIMING, false);
        }
    }
}
