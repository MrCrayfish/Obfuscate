package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: MrCrayfish
 */
@Cancelable
@SideOnly(Side.CLIENT)
public class RenderHeldItemEvent extends Event
{
    private EntityLivingBase entity;
    private ItemStack heldItem;
    private ItemCameraTransforms.TransformType transformType;
    private EnumHandSide handSide;
    private float partialTicks;

    public RenderHeldItemEvent(EntityLivingBase entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float partialTicks)
    {
        this.entity = entity;
        this.heldItem = heldItem;
        this.transformType = transformType;
        this.handSide = handSide;
        this.partialTicks = partialTicks;
    }

    public EntityLivingBase getEntity()
    {
        return entity;
    }

    public ItemStack getHeldItem()
    {
        return heldItem;
    }

    public ItemCameraTransforms.TransformType getTransformType()
    {
        return transformType;
    }

    public EnumHandSide getHandSide()
    {
        return handSide;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }
}
