package com.mrcrayfish.obfuscate.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.obfuscate.client.Hooks;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HeldItemLayer.class)
public class HeldItemLayerMixin 
{
    @Redirect(method = "func_229135_a_", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FirstPersonRenderer;renderItemSide(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"))
    public void fireRenderHeldItem(FirstPersonRenderer renderer, LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light) 
    {
        Hooks.fireRenderHeldItem(renderer, entity, stack, transformType, leftHanded, matrixStack, renderTypeBuffer, light);
    }
}
