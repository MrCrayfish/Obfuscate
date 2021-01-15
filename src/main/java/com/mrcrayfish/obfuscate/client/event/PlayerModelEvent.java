package com.mrcrayfish.obfuscate.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * The base class for player model events.
 *
 * Author: MrCrayfish
 */
public abstract class PlayerModelEvent extends PlayerEvent
{
    private PlayerModel modelPlayer;
    private float partialTicks;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;

    private PlayerModelEvent(PlayerEntity player, PlayerModel modelPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
    {
        super(player);
        this.modelPlayer = modelPlayer;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.partialTicks = partialTicks;
    }

    /**
     * Gets an instance of the player model
     */
    public PlayerModel getModelPlayer()
    {
        return this.modelPlayer;
    }

    /**
     * Gets the current limb swing
     */
    public float getLimbSwing()
    {
        return this.limbSwing;
    }

    /**
     * Gets the current limb swing amount
     */
    public float getLimbSwingAmount()
    {
        return this.limbSwingAmount;
    }

    /**
     * Gets the current age of the player (includes partial ticks)
     */
    public float getAgeInTicks()
    {
        return this.ageInTicks;
    }

    /**
     * Gets the current yaw of the player
     */
    public float getNetHeadYaw()
    {
        return this.netHeadYaw;
    }

    /**
     * Gets the current pitch of the player
     */
    public float getHeadPitch()
    {
        return this.headPitch;
    }

    /**
     * Gets the partial ticks of the current render
     */
    public float getPartialTicks()
    {
        return this.partialTicks;
    }

    /**
     * This event is fired when setting up the rotations of the player's limbs. This can be used to
     * change the pose of the player. A few uses of this event may be to change the pose when holding
     * a certain item or creating a more accurate pose of the player when riding an specific entity.
     * Use {@link Pre} or {@link Post} when subscribing to this event.
     */
    @Cancelable
    public static class SetupAngles extends PlayerModelEvent
    {
        private SetupAngles(PlayerEntity player, PlayerModel modelPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
        {
            super(player, modelPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
        }

        /**
         * Fired before rotations are apply to the players limbs. This can be cancelled to prevent
         * Minecraft from applying rotations to the player and adding your own custom rotations.
         * It should be noted that changing the rotations of the players limbs and not cancelling
         * the event will result in the changes being overridden. Consider using {@link Post} to
         * modify the rotations if you aren't doing a complete custom implementation.
         */
        public static class Pre extends SetupAngles
        {
            public Pre(PlayerEntity player, PlayerModel modelPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
            {
                super(player, modelPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
            }
        }

        /**
         * Fired after rotations have been applied to the player model. This will only be called if
         * {@link Pre} was not cancelled. The benefit of using this over {@link Pre} is that
         * animations such as the player's legs walking will still be included. This event is
         * appropriate for most uses, such as creating a custom pose if holding a certain item. This
         * event cannot be cancelled.
         */
        public static class Post extends SetupAngles
        {
            public Post(PlayerEntity player, PlayerModel modelPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
            {
                super(player, modelPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    /**
     * This event is fired when rendering the player. Unlike {@link net.minecraftforge.client.event.RenderPlayerEvent}
     * from Forge, this event includes transformations applied before rendering the model.
     */
    @Cancelable
    public static class Render extends PlayerModelEvent
    {
        private MatrixStack matrixStack;
        private IVertexBuilder builder;
        private int light;
        private int overlay;

        private Render(PlayerEntity player, PlayerModel modelPlayer, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
        {
            super(player, modelPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
            this.matrixStack = matrixStack;
            this.builder = builder;
            this.light = light;
            this.overlay = overlay;
        }

        /**
         * Fired just before the player model is rendered. It should be noted that cancelling this
         * event will prevent {@link Post} from being fired. Cancelling this event will prevent the
         * player model from being rendered.
         */
        public static class Pre extends Render
        {
            public Pre(PlayerEntity player, PlayerModel modelPlayer, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
            {
                super(player, modelPlayer, matrixStack, builder, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
            }
        }

        /**
         * Called after the player model has been rendered. This event is good for any additional
         * rendering you want to apply to the player. This will only be called if {@link Pre} was
         * not cancelled.
         */
        public static class Post extends Render
        {
            public Post(PlayerEntity player, PlayerModel modelPlayer, MatrixStack matrixStack, IVertexBuilder builder, int light, int overlay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks)
            {
                super(player, modelPlayer, matrixStack, builder, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }

        /**
         * Gets the current matrix stack
         */
        public MatrixStack getMatrixStack()
        {
            return this.matrixStack;
        }

        /**
         * Gets the vertex builder for building entity models
         */
        public IVertexBuilder getBuilder()
        {
            return this.builder;
        }

        /**
         * Gets the current light of the player model
         */
        public int getLight()
        {
            return this.light;
        }

        /**
         * Gets the overlay texture
         */
        public int getOverlay()
        {
            return this.overlay;
        }
    }
}
