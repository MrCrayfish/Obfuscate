package com.mrcrayfish.obfuscate.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
public class Hooks
{
    public static void fireRenderGuiItem(ItemRenderer renderer, ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, IBakedModel model)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Gui.Pre(stack, matrixStack, renderTypeBuffer, light, overlay)))
        {
            renderer.renderItem(stack, transformType, leftHanded, matrixStack, renderTypeBuffer, light, overlay, model);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Gui.Post(stack, matrixStack, renderTypeBuffer, light, overlay));
        }
    }

    public static void fireRenderPlayer(EntityModel model, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if(entity instanceof PlayerEntity && model instanceof PlayerModel)
        {
            if(!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Pre((PlayerEntity) entity, (PlayerModel) model, matrixStack, builder, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks())))
            {
                model.render(matrixStack, builder, light, overlay, red, green, blue, alpha);
                MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Post((PlayerEntity) entity, (PlayerModel) model, matrixStack, builder, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks()));
            }
            return;
        }
        model.render(matrixStack, builder, light, overlay, red, green, blue, alpha);
    }

    public static void fireRenderHeldItem(FirstPersonRenderer renderer, LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light)
    {
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Pre(entity, stack, transformType, matrixStack, renderTypeBuffer, leftHanded ? HandSide.LEFT : HandSide.RIGHT, light, OverlayTexture.NO_OVERLAY, partialTicks)))
        {
            renderer.renderItemSide(entity, stack, transformType, leftHanded, matrixStack, renderTypeBuffer, light);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Post(entity, stack, transformType, matrixStack, renderTypeBuffer, leftHanded ? HandSide.LEFT : HandSide.RIGHT, light, OverlayTexture.NO_OVERLAY, partialTicks));
        }
    }

    public static void fireRenderEntityItem(ItemRenderer renderer, ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, IBakedModel model, ItemEntity entity, float partialTicks)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Pre(entity, stack, matrixStack, renderTypeBuffer, light, overlay, partialTicks)))
        {
            renderer.renderItem(stack, transformType, leftHanded, matrixStack, renderTypeBuffer, light, overlay, model);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Post(entity, stack, matrixStack, renderTypeBuffer, light, overlay, partialTicks));
        }
    }

    public static void fireRenderItemFrameItem(ItemFrameEntity entity, ItemRenderer renderer, ItemStack stack, ItemCameraTransforms.TransformType transformType, int light, int overlay, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.ItemFrame.Pre(entity, stack, matrixStack, renderTypeBuffer, light, overlay, partialTicks)))
        {
            renderer.renderItem(stack, transformType, light, overlay, matrixStack, renderTypeBuffer);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.ItemFrame.Post(entity, stack, matrixStack, renderTypeBuffer, light, overlay, partialTicks));
        }
    }

    public static void fireRenderHeadItem(FirstPersonRenderer renderer, LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Head.Pre(entity, stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, partialTicks)))
        {
            renderer.renderItemSide(entity, stack, transformType, leftHanded, matrixStack, renderTypeBuffer, light);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Head.Post(entity, stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, partialTicks));
        }
    }
}
