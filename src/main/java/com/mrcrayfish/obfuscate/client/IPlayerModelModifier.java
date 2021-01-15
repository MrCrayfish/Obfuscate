package com.mrcrayfish.obfuscate.client;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;

import java.util.List;

/**
 * To be removed in a future update!
 *
 * Author: MrCrayfish
 */
@Deprecated
public interface IPlayerModelModifier
{
    void accept(PlayerRenderer renderer, List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layers, boolean slim);
}
