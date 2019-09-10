package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.Reference;
import com.mrcrayfish.obfuscate.client.model.CustomBipedModel;
import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;
import com.mrcrayfish.obfuscate.client.model.layer.CustomHeldItemLayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
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
            PlayerRenderer render = event.getRenderer();
            Map<String, PlayerRenderer> skinMap = render.getRenderManager().getSkinMap();
            patchPlayerRender(skinMap.get("default"), false);
            patchPlayerRender(skinMap.get("slim"), true);
            setupPlayerRender = true;
        }
    }

    private static void patchPlayerRender(PlayerRenderer player, boolean smallArms)
    {
        PlayerModel<AbstractClientPlayerEntity> model = new CustomPlayerModel(0.0F, smallArms);
        List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, player, "field_177097_h");
        if(layers != null)
        {
            layers.removeIf(layer -> layer instanceof HeldItemLayer || layer instanceof HeadLayer || layer instanceof BipedArmorLayer);
            layers.add(new CustomHeldItemLayer(player));
            layers.add(new HeadLayer<>(player));
            layers.add(new BipedArmorLayer<>(player, new CustomBipedModel(model, 0.5F), new CustomBipedModel(model, 1.0F)));
        }
        ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, player, model, "field_77045_g");
    }
}
