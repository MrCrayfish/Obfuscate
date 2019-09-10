package com.mrcrayfish.obfuscate.client.model.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class CustomHeldItemLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{
    public CustomHeldItemLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer)
    {
        super(renderer);
    }

    @Override
    public void render(AbstractClientPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean isRightHanded = entity.getPrimaryHand() == HandSide.RIGHT;
        ItemStack leftStack = isRightHanded ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
        ItemStack rightStack = isRightHanded ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();

        if(!leftStack.isEmpty() || !rightStack.isEmpty())
        {
            GlStateManager.pushMatrix();

            if(this.getEntityModel().isChild)
            {
                GlStateManager.translatef(0.0F, 0.75F, 0.0F);
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entity, rightStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, partialTicks);
            this.renderHeldItem(entity, leftStack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, partialTicks);
            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, HandSide handSide, float partialTicks)
    {
        if(!stack.isEmpty())
        {
            GlStateManager.pushMatrix();
            {
                if(entity.isSneaking())
                {
                    GlStateManager.translatef(0.0F, 0.2F, 0.0F);
                }
                this.translateToHand(handSide);

                GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);

                boolean isLeftHanded = handSide == HandSide.LEFT;
                GlStateManager.translatef((float) (isLeftHanded ? -1 : 1) / 16.0F, 0.125F, -0.625F);

                if(this.isArmVisible(handSide))
                {
                    if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Pre(entity, stack, transformType, handSide, partialTicks)))
                    {
                        Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, stack, transformType, isLeftHanded);
                        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Post(entity, stack, transformType, handSide, partialTicks));
                    }
                }
            }
            GlStateManager.popMatrix();
        }
    }

    private boolean isArmVisible(HandSide handSide)
    {
        PlayerModel<AbstractClientPlayerEntity> model = this.getEntityModel();
        switch(handSide)
        {
            case LEFT:
                return !model.bipedLeftArm.isHidden && model.bipedLeftArm.showModel;
            case RIGHT:
                return !model.bipedRightArm.isHidden && model.bipedRightArm.showModel;
            default:
                return false;
        }
    }

    private void translateToHand(HandSide handSide)
    {
        this.getEntityModel().postRenderArm(0.0625F, handSide);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
