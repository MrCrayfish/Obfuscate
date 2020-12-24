package com.mrcrayfish.obfuscate.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.Hooks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingRenderer.class)
public class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>> 
{
    private T entity;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/model/EntityModel;render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void capture(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, CallbackInfo ci, boolean shouldSit, float f, float f1, float f2, float f6, float f7, float f8, float f5) 
    {
        this.entity = entity;
        this.limbSwing = f5;
        this.limbSwingAmount = f8;
        this.ageInTicks = f7;
        this.netHeadYaw = f2;
        this.headPitch = f6;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/model/EntityModel;render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V"))
    public void fireRenderPlayer(M model, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) 
    {
        Hooks.fireRenderPlayer(model, matrixStack, builder, light, overlay, red, green, blue, alpha, this.entity, this.limbSwing, this.limbSwingAmount, this.ageInTicks, this.netHeadYaw, this.headPitch);
    }
}
