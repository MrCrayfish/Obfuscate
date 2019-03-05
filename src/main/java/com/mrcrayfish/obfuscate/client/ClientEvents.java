package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.Reference;
import com.mrcrayfish.obfuscate.client.model.CustomModelPlayer;
import com.mrcrayfish.obfuscate.client.model.ModelBipedArmor;
import com.mrcrayfish.obfuscate.client.model.layer.LayerCustomHeldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    private static boolean setupPlayerRender = false;

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        if(!setupPlayerRender)
        {
            Render render = Minecraft.getInstance().getRenderManager().getEntityClassRenderObject(AbstractClientPlayer.class);
            Map<String, RenderPlayer> skinMap = render.getRenderManager().getSkinMap();
            patchPlayerRender(skinMap.get("default"), false);
            patchPlayerRender(skinMap.get("slim"), true);
            setupPlayerRender = true;
        }
    }

    private static void patchPlayerRender(RenderPlayer player, boolean smallArms)
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

    private static void patchArmor(LayerBipedArmor layerBipedArmor, ModelBiped source)
    {
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, layerBipedArmor, new ModelBipedArmor(source, 1.0F), "field_177186_d");
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, layerBipedArmor, new ModelBipedArmor(source, 0.5F), "field_177189_c");
    }
}
