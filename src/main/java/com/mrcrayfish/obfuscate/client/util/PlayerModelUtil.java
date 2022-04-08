package com.mrcrayfish.obfuscate.client.util;

import com.mrcrayfish.obfuscate.client.IPlayerModelModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * To be removed in a future update!
 *
 * Author: MrCrayfish
 */
@Deprecated
public class PlayerModelUtil
{
    /**
     * Adds a layer renderer to the default and slim player model.
     *
     * @param supplier a custom layer renderer supplier
     */
    @Deprecated
    public static void addLayerRenderer(Supplier<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> supplier)
    {
        addLayerRenderer(supplier.get(), supplier.get());
    }

    /**
     * Adds a layer renderer to the player model but with the ability to control based on model type
     *
     * @param defaultLayer a custom layer renderer for the default model
     * @param slimLayer a custom layer renderer for the slim model
     */
    @Deprecated
    public static void addLayerRenderer(LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> defaultLayer, LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> slimLayer)
    {
        modifyPlayerModel((renderer, layers, slim) -> layers.add(slim ? slimLayer : defaultLayer));
    }

    /**
     * Allows for modifications of the player model, such as adding/removing layers. Do not use this
     * to replace {@link PlayerRenderer#entityModel} as Obfuscate provides a custom model to allow
     * for custom limb rotations.
     *
     * @param modifier a player model modifier
     */
    @Deprecated
    @SuppressWarnings("JavadocReference")
    public static void modifyPlayerModel(IPlayerModelModifier modifier)
    {
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        applyPlayerModelModifier(skinMap.get("default"), modifier, false);
        applyPlayerModelModifier(skinMap.get("slim"), modifier, true);
    }

    @Deprecated
    private static void applyPlayerModelModifier(PlayerRenderer renderer, IPlayerModelModifier modifier, boolean slim)
    {
        List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, renderer, "layers");
        if(layers != null)
        {
            modifier.accept(renderer, layers, slim);
        }
    }
}
