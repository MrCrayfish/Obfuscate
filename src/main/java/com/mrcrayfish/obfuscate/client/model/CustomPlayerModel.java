package com.mrcrayfish.obfuscate.client.model;

import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
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

    @Override
    public void setRotationAngles(AbstractClientPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        this.resetRotationAngles();
        if(!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Pre(entityIn, this, Minecraft.getInstance().getRenderPartialTicks())))
        {
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Post(entityIn, this, Minecraft.getInstance().getRenderPartialTicks()));
        }
        this.setupRotationAngles();
    }

    @Override
    public void render(AbstractClientPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.resetVisibilities();
        if(!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Pre(entityIn, this, Minecraft.getInstance().getRenderPartialTicks())))
        {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Post(entityIn, this, Minecraft.getInstance().getRenderPartialTicks()));
        }
    }

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

    private void resetAll(RendererModel renderer)
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
