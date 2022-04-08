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
    private boolean slim;

    @Shadow
    @Final
    public ModelRenderer leftSleeve;

    @Shadow
    @Final
    public ModelRenderer rightSleeve;

    @Shadow
    @Final
    public ModelRenderer leftPants;

    @Shadow
    @Final
    public ModelRenderer rightPants;

    @Shadow
    @Final
    public ModelRenderer jacket;

    public PlayerModelMixin(float modelSize)
    {
        super(modelSize);
    }

    @Inject(method = "setupAnim", at = @At(value = "HEAD"), cancellable = true)
    private void setRotationAnglesHead(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if(!(entityIn instanceof PlayerEntity))
            return;

        PlayerModel model = (PlayerModel) (Object) this;
        this.resetRotationAngles();
        if(MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Pre((PlayerEntity) entityIn, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getFrameTime())))
        {
            this.setupRotationAngles();
            ci.cancel();
        }
    }

    @Inject(method = "setupAnim", at = @At(value = "TAIL"))
    private void setRotationAnglesTail(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if(!(entityIn instanceof PlayerEntity))
            return;

        PlayerModel model = (PlayerModel) (Object) this;
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetupAngles.Post((PlayerEntity) entityIn, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getFrameTime()));
        this.setupRotationAngles();
    }

    private void setupRotationAngles()
    {
        this.rightSleeve.copyFrom(this.rightArm);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftPants.copyFrom(this.leftLeg);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }

    /**
     * Resets all the rotations and rotation points back to their initial values. This makes it
     * so ever developer doesn't have to do it themselves.
     */
    private void resetRotationAngles()
    {
        this.resetAll(this.head);
        this.resetAll(this.hat);
        this.resetAll(this.body);
        this.resetAll(this.jacket);

        this.resetAll(this.rightArm);
        this.rightArm.x = -5.0F;
        this.rightArm.y = this.slim ? 2.5F : 2.0F;
        this.rightArm.z = 0.0F;

        this.resetAll(this.rightSleeve);
        this.rightSleeve.x = -5.0F;
        this.rightSleeve.y = this.slim ? 2.5F : 2.0F;
        this.rightSleeve.z = 10.0F;

        this.resetAll(this.leftArm);
        this.leftArm.x = 5.0F;
        this.leftArm.y = this.slim ? 2.5F : 2.0F;
        this.leftArm.z = 0.0F;

        this.resetAll(this.leftSleeve);
        this.leftSleeve.x = 5.0F;
        this.leftSleeve.y = this.slim ? 2.5F : 2.0F;
        this.leftSleeve.z = 0.0F;

        this.resetAll(this.leftLeg);
        this.leftLeg.x = 1.9F;
        this.leftLeg.y = 12.0F;
        this.leftLeg.z = 0.0F;

        this.resetAll(this.leftPants);
        this.leftPants.copyFrom(this.leftLeg);

        this.resetAll(this.rightLeg);
        this.rightLeg.x = -1.9F;
        this.rightLeg.y = 12.0F;
        this.rightLeg.z = 0.0F;

        this.resetAll(this.rightPants);
        this.rightPants.copyFrom(this.rightLeg);
    }

    /**
     * Resets the rotation angles and points to zero for the given model renderer
     *
     * @param renderer the model part to reset
     */
    private void resetAll(ModelRenderer renderer)
    {
        renderer.xRot = 0.0F;
        renderer.yRot = 0.0F;
        renderer.zRot = 0.0F;
        renderer.x = 0.0F;
        renderer.y = 0.0F;
        renderer.z = 0.0F;
    }

    private void resetVisibilities()
    {
        this.head.visible = false;
        this.body.visible = false;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;
    }
}
