package com.mrcrayfish.obfuscate.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
    private MatrixStack matrixStack;
    private IRenderTypeBuffer renderTypeBuffer;
    private float partialTicks;

    public RenderItemEvent(ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks)
    {
        this.heldItem = heldItem;
        this.transformType = transformType;
        this.matrixStack = matrixStack;
        this.renderTypeBuffer = renderTypeBuffer;
        this.partialTicks = partialTicks;
    }

    public ItemStack getItem()
    {
        return heldItem;
    }

    public ItemCameraTransforms.TransformType getTransformType()
    {
        return transformType;
    }

    public MatrixStack getMatrixStack()
    {
        return matrixStack;
    }

    public IRenderTypeBuffer getRenderTypeBuffer()
    {
        return renderTypeBuffer;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    @Cancelable
    public static class Held extends RenderItemEvent
    {
        private LivingEntity entity;
        private HandSide handSide;

        public Held(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, float partialTicks)
        {
            super(heldItem, transformType, matrixStack, renderTypeBuffer, partialTicks);
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
            public Pre(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, float partialTicks)
            {
                super(entity, heldItem, transformType, matrixStack, renderTypeBuffer, handSide, partialTicks);
            }
        }

        public static class Post extends Held
        {
            public Post(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, float partialTicks)
            {
                super(entity, heldItem, transformType, matrixStack, renderTypeBuffer, handSide, partialTicks);
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

        public Entity(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GROUND, matrixStack, renderTypeBuffer, partialTicks);
            this.entity = entity;
        }

        public ItemEntity getEntity()
        {
            return entity;
        }

        public static class Pre extends Entity
        {
            public Pre(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, partialTicks);
            }
        }

        public static class Post extends Entity
        {
            public Post(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, partialTicks);
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
        public Gui(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GUI, matrixStack, renderTypeBuffer, 0.0F);
        }

        public static class Pre extends Gui
        {
            public Pre(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer)
            {
                super(heldItem, matrixStack, renderTypeBuffer);
            }
        }

        public static class Post extends Gui
        {
            public Post(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer)
            {
                super(heldItem, matrixStack, renderTypeBuffer);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }
}
