package com.mrcrayfish.obfuscate.client.model;

import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: MrCrayfish
 */
public class CustomModelPlayer extends ModelPlayer
{
    private boolean smallArms;

    public CustomModelPlayer(float modelSize, boolean smallArmsIn)
    {
        super(modelSize, smallArmsIn);
        this.smallArms = smallArmsIn;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.resetRotationAngles();
        if(!MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.SetupAngles.Pre((EntityPlayer) entityIn, this, Minecraft.getInstance().getRenderPartialTicks())))
        {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
            MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.SetupAngles.Post((EntityPlayer) entityIn, this, Minecraft.getInstance().getRenderPartialTicks()));
        }
        this.setupRotationAngles();
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.resetVisibilities();
        if(!MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.Render.Pre((EntityPlayer) entityIn, this, Minecraft.getInstance().getRenderPartialTicks())))
        {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.Render.Post((EntityPlayer) entityIn, this, Minecraft.getInstance().getRenderPartialTicks()));
        }
    }

    private void setupRotationAngles()
    {
        copyModelAngles(bipedRightArm, bipedRightArmwear);
        copyModelAngles(bipedLeftArm, bipedLeftArmwear);
        copyModelAngles(bipedRightLeg, bipedRightLegwear);
        copyModelAngles(bipedLeftLeg, bipedLeftLegwear);
        copyModelAngles(bipedBody, bipedBodyWear);
        copyModelAngles(bipedHead, bipedHeadwear);
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
        copyModelAngles(bipedLeftLeg, bipedLeftLegwear);

        this.resetAll(bipedRightLeg);
        bipedRightLeg.rotationPointX = -1.9F;
        bipedRightLeg.rotationPointY = 12.0F;
        bipedRightLeg.rotationPointZ = 0.0F;

        this.resetAll(bipedRightLegwear);
        copyModelAngles(bipedRightLeg, bipedRightLegwear);
    }

    private void resetAll(ModelRenderer renderer)
    {
        renderer.offsetX = 0.0F;
        renderer.offsetY = 0.0F;
        renderer.offsetZ = 0.0F;
        renderer.rotateAngleX = 0.0F;
        renderer.rotateAngleY = 0.0F;
        renderer.rotateAngleZ = 0.0F;
        renderer.rotationPointX = 0.0F;
        renderer.rotationPointY = 0.0F;
        renderer.rotationPointZ = 0.0F;
    }

    private void resetVisibilities()
    {
        this.bipedHead.isHidden = false;
        this.bipedBody.isHidden = false;
        this.bipedRightArm.isHidden = false;
        this.bipedLeftArm.isHidden = false;
        this.bipedRightLeg.isHidden = false;
        this.bipedLeftLeg.isHidden = false;
    }
}
