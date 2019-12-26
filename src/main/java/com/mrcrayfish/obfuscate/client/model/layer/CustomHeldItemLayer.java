package com.mrcrayfish.obfuscate.client.model.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
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
public class CustomHeldItemLayer<T extends LivingEntity, M extends EntityModel<T> & IHasArm> extends LayerRenderer<T, M>
{
    public CustomHeldItemLayer(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    @Override
    public void func_225628_a_(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, T entity, float p_225628_5_, float p_225628_6_, float partialTicks, float p_225628_8_, float p_225628_9_, float p_225628_10_)
    {
        boolean rightHanded = entity.getPrimaryHand() == HandSide.RIGHT;
        ItemStack rightHandStack = rightHanded ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
        ItemStack leftHandStack = rightHanded ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();
        if(!rightHandStack.isEmpty() || !leftHandStack.isEmpty())
        {
            matrixStack.func_227860_a_(); //Push
            if(this.getEntityModel().isChild)
            {
                matrixStack.func_227861_a_(0.0D, 0.75D, 0.0D);
                matrixStack.func_227862_a_(0.5F, 0.5F, 0.5F);
            }
            this.renderHeldItem(entity, leftHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderHeldItem(entity, rightHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStack, renderTypeBuffer, light, partialTicks);
            matrixStack.func_227865_b_(); //Pop
        }
    }

    private void renderHeldItem(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, HandSide handSide, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        if(!stack.isEmpty())
        {
            matrixStack.func_227860_a_();
            this.getEntityModel().func_225599_a_(handSide, matrixStack);
            matrixStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-90.0F));
            matrixStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0F));
            boolean leftHanded = handSide == HandSide.LEFT;
            matrixStack.func_227861_a_((double) ((float) (leftHanded ? -1 : 1) / 16.0F), 0.125D, -0.625D);
            if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Pre(entity, stack, transformType, matrixStack, renderTypeBuffer, handSide, partialTicks)))
            {
                Minecraft.getInstance().getFirstPersonRenderer().func_228397_a_(entity, stack, transformType, leftHanded, matrixStack, renderTypeBuffer, light);
                MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Held.Post(entity, stack, transformType, matrixStack, renderTypeBuffer, handSide, partialTicks));
            }
            matrixStack.func_227865_b_();
        }
    }
}
