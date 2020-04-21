package com.mrcrayfish.obfuscate.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
public class Hooks
{
    public static boolean fireRenderGuiItemPre(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer)
    {
        return MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Gui.Pre(heldItem, matrixStack, renderTypeBuffer, 15728880, OverlayTexture.DEFAULT_LIGHT));
    }

    public static void fireRenderGuiItemPost(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer)
    {
        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Gui.Post(heldItem, matrixStack, renderTypeBuffer, 15728880, OverlayTexture.DEFAULT_LIGHT));
    }

    public static boolean fireRenderPlayerPre(LivingEntity entity, EntityModel model, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if(entity instanceof PlayerEntity)
        {
            return MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Pre((PlayerEntity) entity, (PlayerModel) model, matrixStack, builder, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks()));
        }
        return false;
    }

    public static void fireRenderPlayerPost(LivingEntity entity, EntityModel model, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if(entity instanceof PlayerEntity)
        {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Post((PlayerEntity) entity, (PlayerModel) model, matrixStack, builder, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks()));
        }
    }
}
