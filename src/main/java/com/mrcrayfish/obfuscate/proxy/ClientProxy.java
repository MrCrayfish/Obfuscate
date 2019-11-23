package com.mrcrayfish.obfuscate.proxy;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.client.model.CustomBipedModel;
import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;
import com.mrcrayfish.obfuscate.client.model.layer.CustomHeldItemLayer;
import com.mrcrayfish.obfuscate.client.renderer.entity.CustomItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void setupClient()
    {
        RenderingRegistry.registerEntityRenderingHandler(ItemEntity.class, manager -> new CustomItemRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        this.patchPlayerModels();
    }

    private void patchPlayerModels()
    {
        Obfuscate.LOGGER.info("Starting to patch player models...");

        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        patchPlayerRender(skinMap.get("default"), false);
        patchPlayerRender(skinMap.get("slim"), true);
    }

    private void patchPlayerRender(PlayerRenderer player, boolean smallArms)
    {
        PlayerModel<AbstractClientPlayerEntity> model = new CustomPlayerModel(0.0F, smallArms);
        List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, player, "field_177097_h");
        if(layers != null)
        {
            layers.removeIf(layer -> layer instanceof HeldItemLayer || layer instanceof HeadLayer || layer instanceof BipedArmorLayer);
            layers.add(new CustomHeldItemLayer(player));
            layers.add(new HeadLayer<>(player));
            layers.add(new BipedArmorLayer<>(player, new CustomBipedModel<>(model, 0.5F), new CustomBipedModel<>(model, 1.0F)));
        }
        ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, player, model, "field_77045_g");

        Obfuscate.LOGGER.info("Patched " + (smallArms ? "slim" : "default") + " model successfully");
    }
}
