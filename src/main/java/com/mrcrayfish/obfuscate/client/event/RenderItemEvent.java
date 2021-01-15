package com.mrcrayfish.obfuscate.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Supplier;

/**
 * The base class for item rendering events. It is possible to subscribe to this event to capture
 * all rendering events.
 * <p><br>
 * Although it's possible to control rendering of an item using
 * {@link net.minecraft.item.Item.Properties#setISTER(Supplier)}, the location of the render call is
 * not guaranteed. These events are hooked into the code at the exact rendering calls rather than
 * relying on the camera transform type.
 * <p><br>
 * The current available events are:<br>
 * {@link Held} - called when rendering held item on the player's model<br>
 * {@link Entity} - called when rendering an item on the floor<br>
 * {@link Gui} - called when rendering an item in a GUI<br>
 * {@link ItemFrame} - called when rendering an item in an item frame<br>
 * {@link Head} - called when rendering an item on a player's head (item is in head slot)<br>
 * <p><br>
 * Author: MrCrayfish
 */
@Cancelable
public class RenderItemEvent extends Event
{
    private ItemStack heldItem;
    private ItemCameraTransforms.TransformType transformType;
    private MatrixStack matrixStack;
    private IRenderTypeBuffer renderTypeBuffer;
    private float partialTicks;
    private int light;
    private int overlay;

    public RenderItemEvent(ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
    {
        this.heldItem = heldItem;
        this.transformType = transformType;
        this.matrixStack = matrixStack;
        this.renderTypeBuffer = renderTypeBuffer;
        this.partialTicks = partialTicks;
        this.light = light;
        this.overlay = overlay;
    }

    /**
     * Gets the current stack to be rendered
     */
    public ItemStack getItem()
    {
        return this.heldItem;
    }

    /**
     * Gets the transform type of the render
     */
    public ItemCameraTransforms.TransformType getTransformType()
    {
        return this.transformType;
    }

    /**
     * Gets the current matrix stack
     */
    public MatrixStack getMatrixStack()
    {
        return this.matrixStack;
    }

    /**
     * Gets an instance of the render type buffer
     */
    public IRenderTypeBuffer getRenderTypeBuffer()
    {
        return this.renderTypeBuffer;
    }

    /**
     * Gets the light for the current render
     */
    public int getLight()
    {
        return this.light;
    }

    /**
     * Gets the overlay texture for the render
     */
    public int getOverlay()
    {
        return this.overlay;
    }

    /**
     * Gets the current partial ticks
     */
    public float getPartialTicks()
    {
        return this.partialTicks;
    }

    /**
     * This event is fired when rendering the held item on the player's model. This is only called
     * when looking at other players or when the current point of view is third person. Use
     * {@link Pre} or {@link Post} when subscribing to this event.
     */
    @Cancelable
    public static class Held extends RenderItemEvent
    {
        private LivingEntity entity;
        private HandSide handSide;

        public Held(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, int light, int overlay, float partialTicks)
        {
            super(heldItem, transformType, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            this.entity = entity;
            this.handSide = handSide;
        }

        /**
         * Gets the entity holding the item
         */
        public LivingEntity getEntity()
        {
            return this.entity;
        }

        /**
         * Gets the hand side that is holding the item
         */
        public HandSide getHandSide()
        {
            return this.handSide;
        }

        /**
         * Called before the item is rendered in the players hand. This event can be cancelled to
         * prevent the item from being rendered and perform any custom rendering. It should be noted
         * that cancelling this event will prevent {@link Post} from being called.
         */
        public static class Pre extends Held
        {
            public Pre(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, transformType, matrixStack, renderTypeBuffer, handSide, light, overlay, partialTicks);
            }
        }

        /**
         * Called after the item has been rendered and if {@link Pre} has not been cancelled. This
         * event cannot be cancelled.
         */
        public static class Post extends Held
        {
            public Post(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, transformType, matrixStack, renderTypeBuffer, handSide, light, overlay, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    /**
     * This event is fired when rendering an entity item. Use {@link Pre} or {@link Post} when
     * subscribing to this event.
     */
    @Cancelable
    public static class Entity extends RenderItemEvent
    {
        private ItemEntity entity;

        public Entity(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GROUND, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            this.entity = entity;
        }

        /**
         * Gets the item entity being rendered
         */
        public ItemEntity getEntity()
        {
            return this.entity;
        }

        /**
         * Called before the item is rendered on the ground. This event can be cancelled to
         * prevent the item from being rendered and perform any custom rendering. It should be noted
         * that cancelling this event will prevent {@link Post} from being called.
         */
        public static class Pre extends Entity
        {
            public Pre(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }
        }

        /**
         * Called after the item has been rendered and if {@link Pre} has not been cancelled. This
         * event cannot be cancelled.
         */
        public static class Post extends Entity
        {
            public Post(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    /**
     * This event is fired when rendering an item in an inventory or on the hotbar. Use
     * {@link Pre} or {@link Post} when subscribing to this event.
     */
    @Cancelable
    public static class Gui extends RenderItemEvent
    {
        public Gui(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GUI, matrixStack, renderTypeBuffer, light, overlay, Minecraft.getInstance().getRenderPartialTicks());
        }

        /**
         * Called before the item is rendered in the GUI. This event can be cancelled to prevent the
         * item from being rendered and perform any custom rendering. It should be noted that
         * cancelling this event will prevent {@link Post} from being called.
         */
        public static class Pre extends Gui
        {
            public Pre(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
            {
                super(heldItem, matrixStack, renderTypeBuffer, light, overlay);
            }
        }

        /**
         * Called after the item has been rendered and if {@link Pre} has not been cancelled. This
         * event cannot be cancelled.
         */
        public static class Post extends Gui
        {
            public Post(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
            {
                super(heldItem, matrixStack, renderTypeBuffer, light, overlay);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    /**
     * This event is fired when rendering an item on an item frame. Use {@link Pre} or
     * {@link Post} when subscribing to this event.
     */
    @Cancelable
    public static class ItemFrame extends RenderItemEvent
    {
        private ItemFrameEntity entity;

        public ItemFrame(ItemFrameEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            this.entity = entity;
        }

        /**
         * Gets the item frame entity that is holding the item
         */
        public ItemFrameEntity getEntity()
        {
            return this.entity;
        }

        /**
         * Called before the item is rendered into the item frame. This event can be cancelled to
         * prevent the item from being rendered and perform any custom rendering. It should be noted
         * that cancelling this event will prevent {@link Post} from being called.
         */
        public static class Pre extends ItemFrame
        {
            public Pre(ItemFrameEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }
        }

        /**
         * Called after the item has been rendered and if {@link Pre} has not been cancelled. This
         * event cannot be cancelled.
         */
        public static class Post extends ItemFrame
        {
            public Post(ItemFrameEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    /**
     * This event is fired when rendering an item on a players head. The item that is being rendered
     * is the one that is currently in the head slot of the player. Use {@link Pre} or {@link Post}
     * when subscribing to this event.
     */
    @Cancelable
    public static class Head extends RenderItemEvent
    {
        private LivingEntity entity;

        public Head(LivingEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            this.entity = entity;
        }

        /**
         * Gets the entity that has the item being rendered to it's head
         */
        public LivingEntity getEntity()
        {
            return this.entity;
        }

        /**
         * Called before the item is rendered on the players head. This event can be cancelled to
         * prevent the item from being rendered and perform any custom rendering. It should be noted
         * that cancelling this event will prevent {@link Post} from being called.
         */
        public static class Pre extends Head
        {
            public Pre(LivingEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }
        }

        /**
         * Called after the item has been rendered and if {@link Pre} has not been cancelled. This
         * event cannot be cancelled.
         */
        public static class Post extends Head
        {
            public Post(LivingEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }
}
