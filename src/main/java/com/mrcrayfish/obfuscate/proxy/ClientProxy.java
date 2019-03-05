package com.mrcrayfish.obfuscate.proxy;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.client.renderer.entity.RenderCustomEntityItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Author: MrCrayfish
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, manager -> new RenderCustomEntityItem(manager, Minecraft.getInstance().getItemRenderer()));
    }
}
