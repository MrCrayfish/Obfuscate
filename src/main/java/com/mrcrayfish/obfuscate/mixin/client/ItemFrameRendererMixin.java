package com.mrcrayfish.obfuscate.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.obfuscate.client.Hooks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameRenderer.class)
public class ItemFrameRendererMixin 
{
    private float partialTicks;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;IILcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;)V"))
    public void capture(ItemFrameEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, CallbackInfo ci) 
    {
        this.partialTicks = partialTicks;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;IILcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;)V"))
    public void fireRenderItemFrameItem(ItemRenderer renderer, ItemStack stack, ItemCameraTransforms.TransformType transformType, int light, int overlay, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer) 
    {
        Hooks.fireRenderItemFrameItem(renderer, stack, transformType, light, overlay, matrixStack, renderTypeBuffer, this.partialTicks);
    }
}
