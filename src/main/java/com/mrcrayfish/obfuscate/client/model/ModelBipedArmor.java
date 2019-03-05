package com.mrcrayfish.obfuscate.client.model;

import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.Entity;

/**
 * Author: MrCrayfish
 */
public class ModelBipedArmor extends ModelBiped
{
    private ModelBiped source;

    public ModelBipedArmor(ModelBiped source, float modelSize)
    {
        super(modelSize);
        this.source = source;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        copyModelAngles(source.bipedHeadwear, bipedHeadwear);
        copyModelAngles(source.bipedHead, bipedHead);
        copyModelAngles(source.bipedBody, bipedBody);
        copyModelAngles(source.bipedRightArm, bipedRightArm);
        copyModelAngles(source.bipedLeftArm, bipedLeftArm);
        copyModelAngles(source.bipedRightLeg, bipedRightLeg);
        copyModelAngles(source.bipedLeftLeg, bipedLeftLeg);
    }
}
