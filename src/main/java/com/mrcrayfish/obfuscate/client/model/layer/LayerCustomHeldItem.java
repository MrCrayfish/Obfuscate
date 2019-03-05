package com.mrcrayfish.obfuscate.client.model.layer;

import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class LayerCustomHeldItem implements LayerRenderer<EntityLivingBase>
{
    private final RenderLivingBase<?> livingEntityRenderer;

    public LayerCustomHeldItem(RenderLivingBase<?> livingEntityRendererIn)
    {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean isRightHanded = entity.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack leftStack = isRightHanded ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
        ItemStack rightStack = isRightHanded ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();

        if(!leftStack.isEmpty() || !rightStack.isEmpty())
        {
            GlStateManager.pushMatrix();

            if(this.livingEntityRenderer.getMainModel().isChild)
            {
                GlStateManager.translatef(0.0F, 0.75F, 0.0F);
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entity, rightStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT, partialTicks);
            this.renderHeldItem(entity, leftStack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT, partialTicks);
            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float partialTicks)
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

                boolean isLeftHanded = handSide == EnumHandSide.LEFT;
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

    private boolean isArmVisible(EnumHandSide handSide)
    {
        RenderPlayer render = (RenderPlayer) livingEntityRenderer;
        switch(handSide)
        {
            case LEFT:
                return !render.getMainModel().bipedLeftArm.isHidden && render.getMainModel().bipedLeftArm.showModel;
            case RIGHT:
                return !render.getMainModel().bipedRightArm.isHidden && render.getMainModel().bipedRightArm.showModel;
            default:
                return false;
        }
    }

    private void translateToHand(EnumHandSide p_191361_1_)
    {
        ((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
