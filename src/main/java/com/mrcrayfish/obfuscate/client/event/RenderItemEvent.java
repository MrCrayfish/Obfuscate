package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

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

    public static class Held extends RenderItemEvent
    {
        private EntityLivingBase entity;
        private EnumHandSide handSide;

        public Held(EntityLivingBase entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float partialTicks)
        {
            super(heldItem, transformType, partialTicks);
            this.entity = entity;
            this.handSide = handSide;
        }

        public EntityLivingBase getEntity()
        {
            return entity;
        }

        public EnumHandSide getHandSide()
        {
            return handSide;
        }
    }

    public static class Entity extends RenderItemEvent
    {
        private EntityItem entity;

        public Entity(EntityItem entity, ItemStack heldItem, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GROUND, partialTicks);
            this.entity = entity;
        }

        public EntityItem getEntity()
        {
            return entity;
        }

        @Cancelable
        public static class Pre extends Entity
        {
            public Pre(EntityItem entity, ItemStack heldItem, float partialTicks)
            {
                super(entity, heldItem, partialTicks);
            }
        }

        public static class Post extends Entity
        {
            public Post(EntityItem entity, ItemStack heldItem, float partialTicks)
            {
                super(entity, heldItem, partialTicks);
            }
        }
    }

    @Cancelable
    public static class Gui extends RenderItemEvent
    {
        public Gui(ItemStack heldItem)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GROUND, 1.0F);
        }
    }
}
