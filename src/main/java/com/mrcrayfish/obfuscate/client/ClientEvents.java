package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.client.model.ModelBipedArmor;
import com.mrcrayfish.obfuscate.client.model.layer.LayerCustomHeldItem;
import com.mrcrayfish.obfuscate.client.model.CustomModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        ModelBiped model = new CustomModelPlayer(0.0F, smallArms);
        List<LayerRenderer<EntityLivingBase>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, player, "field_177097_h");
        if(layers != null)
        {
            layers.removeIf(layerRenderer -> layerRenderer instanceof LayerHeldItem || layerRenderer instanceof LayerCustomHead);
            layers.add(new LayerCustomHeldItem(player));
            layers.add(new LayerCustomHead(model.bipedHead));
            layers.forEach(layerRenderer ->
            {
                if(layerRenderer instanceof LayerBipedArmor)
                {
                    patchArmor((LayerBipedArmor) layerRenderer, model);
                }
            });
        }
        ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, player, model, "field_77045_g");
    }

    private void patchArmor(LayerBipedArmor layerBipedArmor, ModelBiped source)
    {
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, layerBipedArmor, new ModelBipedArmor(source, 1.0F), "field_177186_d");
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, layerBipedArmor, new ModelBipedArmor(source, 0.5F), "field_177189_c");
    }
}
