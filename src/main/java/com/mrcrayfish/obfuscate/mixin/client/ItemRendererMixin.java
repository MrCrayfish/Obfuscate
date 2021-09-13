package com.mrcrayfish.obfuscate.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.obfuscate.client.Hooks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin 
{
    @Redirect(method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
    public void fireRenderGuiItem(ItemRenderer renderer, ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHanded, PoseStack poseStack, MultiBufferSource source, int light, int overlay, BakedModel model)
    {
        Hooks.fireRenderGuiItem(renderer, stack, transformType, leftHanded, poseStack, source, light, overlay, model);
    }
}
