package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.client.model.layer.LayerCustomHeldItem;
import com.mrcrayfish.obfuscate.client.model.CustomModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ClientEvents
{
    private boolean setupPlayerRender = false;

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        if(!setupPlayerRender)
        {
            Render render = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(AbstractClientPlayer.class);
            Map<String, RenderPlayer> skinMap = render.getRenderManager().getSkinMap();
            this.patchPlayerRender(skinMap.get("default"), false);
            this.patchPlayerRender(skinMap.get("slim"), true);
            setupPlayerRender = true;
        }
    }

    private void patchPlayerRender(RenderPlayer player, boolean smallArms)
    {
        List<LayerRenderer<EntityLivingBase>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, player, "field_177097_h");
        if(layers != null)
        {
            layers.removeIf(layerRenderer -> layerRenderer instanceof LayerHeldItem);
            layers.add(new LayerCustomHeldItem(player));
        }
        ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, player, new CustomModelPlayer(0.0F, smallArms), "field_77045_g");
    }
}
