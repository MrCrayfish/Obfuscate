package com.mrcrayfish.obfuscate.proxy;

import com.mrcrayfish.obfuscate.client.ClientEvents;
import com.mrcrayfish.obfuscate.client.renderer.entity.RenderCustomEntityItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Author: MrCrayfish
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, manager -> new RenderCustomEntityItem(manager, Minecraft.getMinecraft().getRenderItem()));
    }
}
