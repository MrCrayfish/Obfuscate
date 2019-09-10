package com.mrcrayfish.obfuscate.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;

/**
 * Author: MrCrayfish
 */
public class CustomBipedModel extends BipedModel
{
    private BipedModel source;

    public CustomBipedModel(BipedModel source, float modelSize)
    {
        super(modelSize);
        this.source = source;
    }

    @Override
    public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        bipedHeadwear.copyModelAngles(source.bipedHeadwear);
        bipedHead.copyModelAngles(source.bipedHead);
        bipedBody.copyModelAngles(source.bipedBody);
        bipedRightArm.copyModelAngles(source.bipedRightArm);
        bipedLeftArm.copyModelAngles(source.bipedLeftArm);
        bipedRightLeg.copyModelAngles(source.bipedRightLeg);
        bipedLeftLeg.copyModelAngles(source.bipedLeftLeg);
    }
}
