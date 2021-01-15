package com.mrcrayfish.obfuscate.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
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

    public ItemStack getItem()
    {
        return this.heldItem;
    }

    public ItemCameraTransforms.TransformType getTransformType()
    {
        return this.transformType;
    }

    public MatrixStack getMatrixStack()
    {
        return this.matrixStack;
    }

    public IRenderTypeBuffer getRenderTypeBuffer()
    {
        return this.renderTypeBuffer;
    }

    public int getLight()
    {
        return this.light;
    }

    public int getOverlay()
    {
        return this.overlay;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }

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

        public LivingEntity getEntity()
        {
            return this.entity;
        }

        public HandSide getHandSide()
        {
            return this.handSide;
        }

        public static class Pre extends Held
        {
            public Pre(LivingEntity entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, HandSide handSide, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, transformType, matrixStack, renderTypeBuffer, handSide, light, overlay, partialTicks);
            }
        }

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

    @Cancelable
    public static class Entity extends RenderItemEvent
    {
        private ItemEntity entity;

        public Entity(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GROUND, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            this.entity = entity;
        }

        public ItemEntity getEntity()
        {
            return this.entity;
        }

        public static class Pre extends Entity
        {
            public Pre(ItemEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }
        }

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

    @Cancelable
    public static class Gui extends RenderItemEvent
    {
        public Gui(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
        {
            super(heldItem, ItemCameraTransforms.TransformType.GUI, matrixStack, renderTypeBuffer, light, overlay, Minecraft.getInstance().getRenderPartialTicks());
        }

        public static class Pre extends Gui
        {
            public Pre(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
            {
                super(heldItem, matrixStack, renderTypeBuffer, light, overlay);
            }
        }

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

    @Cancelable
    public static class ItemFrame extends RenderItemEvent
    {
        public ItemFrame(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
        }

        public static class Pre extends ItemFrame
        {
            public Pre(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }
        }

        public static class Post extends ItemFrame
        {
            public Post(ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    @Cancelable
    public static class Head extends RenderItemEvent
    {
        private LivingEntity entity;

        public Head(LivingEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
        {
            super(heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            this.entity = entity;
        }

        public LivingEntity getEntity()
        {
            return this.entity;
        }

        public static class Pre extends Head
        {
            public Pre(LivingEntity entity, ItemStack heldItem, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay, float partialTicks)
            {
                super(entity, heldItem, matrixStack, renderTypeBuffer, light, overlay, partialTicks);
            }
        }

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
