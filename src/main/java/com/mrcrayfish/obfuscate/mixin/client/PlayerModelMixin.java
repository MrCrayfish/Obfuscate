package com.mrcrayfish.obfuscate.mixin.client;

import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: MrCrayfish
 */
@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends BipedModel<T>
{
    @Shadow
    @Final
    private boolean smallArms;

    @Shadow
    @Final
    public ModelRenderer bipedLeftArmwear;

    @Shadow
    @Final
    public ModelRenderer bipedRightArmwear;

    @Shadow
    @Final
    public ModelRenderer bipedLeftLegwear;

    @Shadow
    @Final
    public ModelRenderer bipedRightLegwear;

    @Shadow
    @Final
    public ModelRenderer bipedBodyWear;

    public PlayerModelMixin(float modelSize)
    {
        super(modelSize);
    }

    @Inject(method = "setRotationAngles", at = @At(value = "HEAD"), cancellable = true)
    private void setRotationAnglesHead(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if(!(entityIn instanceof PlayerEntity))
            return;

        PlayerModel model = (PlayerModel) (Object) this;
        this.resetRotationAngles();
        if(MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Pre((PlayerEntity) entityIn, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks())))
        {
            this.setupRotationAngles();
            ci.cancel();
        }
    }

    @Inject(method = "setRotationAngles", at = @At(value = "TAIL"))
    private void setRotationAnglesTail(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if(!(entityIn instanceof PlayerEntity))
            return;

        PlayerModel model = (PlayerModel) (Object) this;
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Post((PlayerEntity) entityIn, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getRenderPartialTicks()));
        this.setupRotationAngles();
    }

    private void setupRotationAngles()
    {
        this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
        this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
        this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
        this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
        this.bipedBodyWear.copyModelAngles(this.bipedBody);
        this.bipedHeadwear.copyModelAngles(this.bipedHead);
    }

    /**
     * Resets all the rotations and rotation points back to their initial values. This makes it
     * so ever developer doesn't have to do it themselves.
     */
    private void resetRotationAngles()
    {
        this.resetAll(this.bipedHead);
        this.resetAll(this.bipedHeadwear);
        this.resetAll(this.bipedBody);
        this.resetAll(this.bipedBodyWear);

        this.resetAll(this.bipedRightArm);
        this.bipedRightArm.rotationPointX = -5.0F;
        this.bipedRightArm.rotationPointY = this.smallArms ? 2.5F : 2.0F;
        this.bipedRightArm.rotationPointZ = 0.0F;

        this.resetAll(this.bipedRightArmwear);
        this.bipedRightArmwear.rotationPointX = -5.0F;
        this.bipedRightArmwear.rotationPointY = this.smallArms ? 2.5F : 2.0F;
        this.bipedRightArmwear.rotationPointZ = 10.0F;

        this.resetAll(this.bipedLeftArm);
        this.bipedLeftArm.rotationPointX = 5.0F;
        this.bipedLeftArm.rotationPointY = this.smallArms ? 2.5F : 2.0F;
        this.bipedLeftArm.rotationPointZ = 0.0F;

        this.resetAll(this.bipedLeftArmwear);
        this.bipedLeftArmwear.rotationPointX = 5.0F;
        this.bipedLeftArmwear.rotationPointY = this.smallArms ? 2.5F : 2.0F;
        this.bipedLeftArmwear.rotationPointZ = 0.0F;

        this.resetAll(this.bipedLeftLeg);
        this.bipedLeftLeg.rotationPointX = 1.9F;
        this.bipedLeftLeg.rotationPointY = 12.0F;
        this.bipedLeftLeg.rotationPointZ = 0.0F;

        this.resetAll(this.bipedLeftLegwear);
        this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);

        this.resetAll(this.bipedRightLeg);
        this.bipedRightLeg.rotationPointX = -1.9F;
        this.bipedRightLeg.rotationPointY = 12.0F;
        this.bipedRightLeg.rotationPointZ = 0.0F;

        this.resetAll(this.bipedRightLegwear);
        this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
    }

    /**
     * Resets the rotation angles and points to zero for the given model renderer
     *
     * @param renderer the model part to reset
     */
    private void resetAll(ModelRenderer renderer)
    {
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
