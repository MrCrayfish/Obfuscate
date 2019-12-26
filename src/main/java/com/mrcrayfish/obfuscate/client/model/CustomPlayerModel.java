package com.mrcrayfish.obfuscate.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
public class CustomPlayerModel extends PlayerModel<AbstractClientPlayerEntity>
{
    private boolean smallArms;

    public CustomPlayerModel(float modelSize, boolean smallArmsIn)
    {
        super(modelSize, smallArmsIn);
        this.smallArms = smallArmsIn;
    }

    //func_225597_a_

    @Override
    public void func_225597_a_(AbstractClientPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.resetRotationAngles();
        if(!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Pre(entityIn, this, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks())))
        {
            super.func_225597_a_(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Post(entityIn, this, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks()));
        }
        this.setupRotationAngles();
    }

    //func_225598_a_

    @Override
    public void func_225598_a_(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.func_225598_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        /*this.resetVisibilities();
        if(!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Pre(entityIn, this, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, Minecraft.getInstance().getRenderPartialTicks())))
        {
            super.func_225598_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Post(entityIn, this, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, Minecraft.getInstance().getRenderPartialTicks()));
        }*/

    }

    /*@Override
    public void render(AbstractClientPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {


    }*/

    private void setupRotationAngles()
    {
        bipedRightArmwear.copyModelAngles(bipedRightArm);
        bipedLeftArmwear.copyModelAngles(bipedLeftArm);
        bipedRightLegwear.copyModelAngles(bipedRightLeg);
        bipedLeftLegwear.copyModelAngles(bipedLeftLeg);
        bipedBodyWear.copyModelAngles(bipedBody);
        bipedHeadwear.copyModelAngles(bipedHead);
    }

    private void resetRotationAngles()
    {
        this.resetAll(bipedHead);
        this.resetAll(bipedHeadwear);
        this.resetAll(bipedBody);
        this.resetAll(bipedBodyWear);

        this.resetAll(bipedRightArm);
        bipedRightArm.rotationPointX = -5.0F;
        bipedRightArm.rotationPointY = smallArms ? 2.5F : 2.0F;
        bipedRightArm.rotationPointZ = 0.0F;

        this.resetAll(bipedRightArmwear);
        bipedRightArmwear.rotationPointX = -5.0F;
        bipedRightArmwear.rotationPointY = smallArms ? 2.5F : 2.0F;
        bipedRightArmwear.rotationPointZ = 10.0F;

        this.resetAll(bipedLeftArm);
        bipedLeftArm.rotationPointX = 5.0F;
        bipedLeftArm.rotationPointY = smallArms ? 2.5F : 2.0F;
        bipedLeftArm.rotationPointZ = 0.0F;

        this.resetAll(bipedLeftArmwear);
        bipedLeftArmwear.rotationPointX = 5.0F;
        bipedLeftArmwear.rotationPointY = smallArms ? 2.5F : 2.0F;
        bipedLeftArmwear.rotationPointZ = 0.0F;

        this.resetAll(bipedLeftLeg);
        bipedLeftLeg.rotationPointX = 1.9F;
        bipedLeftLeg.rotationPointY = 12.0F;
        bipedLeftLeg.rotationPointZ = 0.0F;

        this.resetAll(bipedLeftLegwear);
        bipedLeftLegwear.copyModelAngles(bipedLeftLeg);

        this.resetAll(bipedRightLeg);
        bipedRightLeg.rotationPointX = -1.9F;
        bipedRightLeg.rotationPointY = 12.0F;
        bipedRightLeg.rotationPointZ = 0.0F;

        this.resetAll(bipedRightLegwear);
        bipedRightLegwear.copyModelAngles(bipedRightLeg);
    }

    private void resetAll(ModelRenderer renderer)
    {
        /*renderer.offsetX = 0.0F;
        renderer.offsetY = 0.0F;
        renderer.offsetZ = 0.0F;*/
        renderer.rotateAngleX = 0.0F;
        renderer.rotateAngleY = 0.0F;
        renderer.rotateAngleZ = 0.0F;
        renderer.rotationPointX = 0.0F;
        renderer.rotationPointY = 0.0F;
        renderer.rotationPointZ = 0.0F;
    }

    private void resetVisibilities()
    {
        this.bipedHead.showModel = false;
        this.bipedBody.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftArm.showModel = false;
        this.bipedRightLeg.showModel = false;
        this.bipedLeftLeg.showModel = false;
    }
}
