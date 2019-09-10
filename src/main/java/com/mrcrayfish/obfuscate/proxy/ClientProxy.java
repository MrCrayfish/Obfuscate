package com.mrcrayfish.obfuscate.proxy;

import com.mrcrayfish.obfuscate.client.renderer.entity.CustomItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Author: MrCrayfish
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void setupClient()
    {
        RenderingRegistry.registerEntityRenderingHandler(ItemEntity.class, manager -> new CustomItemRenderer(manager, Minecraft.getInstance().getItemRenderer()));
    }
}
