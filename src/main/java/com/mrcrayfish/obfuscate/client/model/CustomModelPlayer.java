package com.mrcrayfish.obfuscate.client.model;

import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
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
        if(!MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.SetupAngles.Pre((EntityPlayer) entityIn, this, ageInTicks - entityIn.ticksExisted)))
        {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
            MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.SetupAngles.Post((EntityPlayer) entityIn, this, ageInTicks - entityIn.ticksExisted));
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.resetRotationAngles();
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
}
