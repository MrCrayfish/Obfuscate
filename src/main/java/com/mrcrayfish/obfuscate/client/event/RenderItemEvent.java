package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Author: MrCrayfish
 */
public class RenderItemEvent extends Event
{
    private ItemStack heldItem;
    private ItemCameraTransforms.TransformType transformType;
    private float partialTicks;

    public RenderItemEvent(ItemStack heldItem, ItemCameraTransforms.TransformType transformType, float partialTicks)
    {
        this.heldItem = heldItem;
        this.transformType = transformType;
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    public ItemStack getItem()
    {
        return heldItem;
    }

    public ItemCameraTransforms.TransformType getTransformType()
    {
        return transformType;
    }

    @Cancelable
    public static class Held extends RenderItemEvent
    {
        private LivingEntity entity;
        private HandSide handSide;

        public Held(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, HandSide handSide, float partialTicks)
        {
            super(heldItem, transformType, partialTicks);
            this.entity = entity;
            this.handSide = handSide;
        }

        public LivingEntity getEntity()
        {
            return entity;
        }

        public HandSide getHandSide()
        {
            return handSide;
        }

        public static class Pre extends Held
        {
            public Pre(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, HandSide handSide, float partialTicks)
            {
                super(entity, heldItem, transformType, handSide, partialTicks);
            }
        }

        public static class Post extends Held
        {
            public Post(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, HandSide handSide, float partialTicks)
            {
                super(entity, heldItem, transformType, handSide, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    @Cancelable
    public static class Entity extends RenderItemEvent
    {
        private ItemEntity entity;

        public Entity(ItemEntity entity, ItemStack heldItem, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GROUND, partialTicks);
            this.entity = entity;
        }

        public ItemEntity getEntity()
        {
            return entity;
        }

        public static class Pre extends Entity
        {
            public Pre(ItemEntity entity, ItemStack heldItem, float partialTicks)
            {
                super(entity, heldItem, partialTicks);
            }
        }

        public static class Post extends Entity
        {
            public Post(ItemEntity entity, ItemStack heldItem, float partialTicks)
            {
                super(entity, heldItem, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    @Cancelable
    public static class Gui extends RenderItemEvent
    {
        public Gui(ItemStack heldItem)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GUI, 0.0F);
        }

        public static class Pre extends Gui
        {
            public Pre(ItemStack heldItem)
            {
                super(heldItem);
            }
        }

        public static class Post extends Gui
        {
            public Post(ItemStack heldItem)
            {
                super(heldItem);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }
}
