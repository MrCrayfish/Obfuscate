package com.mrcrayfish.obfuscate.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
public class Hooks
{
    public static void fireRenderGuiItem(ItemRenderer renderer, ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHanded, PoseStack poseStack, MultiBufferSource source, int light, int overlay, BakedModel model)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Gui.Pre(stack, poseStack, source, light, overlay)))
        {
            renderer.render(stack, transformType, leftHanded, poseStack, source, light, overlay, model);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Gui.Post(stack, poseStack, source, light, overlay));
        }
    }

    public static void fireRenderPlayer(EntityModel model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if(entity instanceof Player && model instanceof PlayerModel)
        {
            if(!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Pre((Player) entity, (PlayerModel) model, poseStack, consumer, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getDeltaFrameTime())))
            {
                model.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
                MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Post((Player) entity, (PlayerModel) model, poseStack, consumer, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getDeltaFrameTime()));
            }
            return;
        }
        model.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
    }

    public static void fireRenderHeldItem(ItemInHandRenderer renderer, LivingEntity entity, ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHanded, PoseStack poseStack, MultiBufferSource source, int light)
    {
        float deltaTick = Minecraft.getInstance().getDeltaFrameTime();
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Pre(entity, stack, transformType, poseStack, source, leftHanded ? HumanoidArm.LEFT : HumanoidArm.RIGHT, light, OverlayTexture.NO_OVERLAY, deltaTick)))
        {
            renderer.renderItem(entity, stack, transformType, leftHanded, poseStack, source, light);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Post(entity, stack, transformType, poseStack, source, leftHanded ? HumanoidArm.LEFT : HumanoidArm.RIGHT, light, OverlayTexture.NO_OVERLAY, deltaTick));
        }
    }

    public static void fireRenderEntityItem(ItemRenderer renderer, ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHanded, PoseStack poseStack, MultiBufferSource source, int light, int overlay, BakedModel model, ItemEntity entity, float deltaTick)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Pre(entity, stack, poseStack, source, light, overlay, deltaTick)))
        {
            renderer.render(stack, transformType, leftHanded, poseStack, source, light, overlay, model);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Post(entity, stack, poseStack, source, light, overlay, deltaTick));
        }
    }

    public static void fireRenderItemFrameItem(ItemFrame entity, ItemRenderer renderer, ItemStack stack, ItemTransforms.TransformType transformType, int light, int overlay, PoseStack poseStack, MultiBufferSource source, float deltaTick)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.ItemFrame.Pre(entity, stack, poseStack, source, light, overlay, deltaTick)))
        {
            renderer.renderStatic(stack, transformType, light, overlay, poseStack, source, entity.getId());
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.ItemFrame.Post(entity, stack, poseStack, source, light, overlay, deltaTick));
        }
    }

    public static void fireRenderHeadItem(ItemInHandRenderer renderer, LivingEntity entity, ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHanded, PoseStack poseStack, MultiBufferSource source, int light, float deltaTick)
    {
        if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Head.Pre(entity, stack, poseStack, source, light, OverlayTexture.NO_OVERLAY, deltaTick)))
        {
            renderer.renderItem(entity, stack, transformType, leftHanded, poseStack, source, light);
            MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Head.Post(entity, stack, poseStack, source, light, OverlayTexture.NO_OVERLAY, deltaTick));
        }
    }
}
