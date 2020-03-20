package com.mrcrayfish.obfuscate.client.renderer.entity;

import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class RenderCustomEntityItem extends RenderEntityItem
{
    private final RenderItem renderItem;
    private final Random random = new Random();

    public RenderCustomEntityItem(RenderManager renderManagerIn, RenderItem renderItem)
    {
        super(renderManagerIn, renderItem);
        this.renderItem = renderItem;
    }

    private int transformModelCount(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
        ItemStack itemstack = itemIn.getItem();
        Item item = itemstack.getItem();

        if(item == null)
        {
            return 0;
        }
        else
        {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.getModelCount(itemstack);
            float f = 0.25F;
            float f1 = shouldBob() ? MathHelper.sin(((float) itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float) p_177077_2_, (float) p_177077_4_ + f1 + 0.25F * f2, (float) p_177077_6_);

            if(flag || this.renderManager.options != null)
            {
                float f3 = (((float) itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float) Math.PI);
                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ItemStack stack = entity.getItem();
        int seed = stack.isEmpty() ? 187 : Item.getIdFromItem(stack.getItem()) + stack.getMetadata();
        this.random.setSeed((long) seed);
        boolean flag = false;

        if(this.bindEntityTexture(entity))
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel model = this.renderItem.getItemModelWithOverrides(stack, entity.world, (EntityLivingBase) null);
        int modelCount = this.transformModelCount(entity, x, y, z, partialTicks, model);
        boolean is3D = model.isGui3d();

        if(!is3D)
        {
            float offsetX = -0.0F * (float) (modelCount - 1) * 0.5F;
            float offsetY = -0.0F * (float) (modelCount - 1) * 0.5F;
            float offsetZ = -0.09375F * (float) (modelCount - 1) * 0.5F;
            GlStateManager.translate(offsetX, offsetY, offsetZ);
        }

        if(this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        for(int k = 0; k < modelCount; ++k)
        {
            if(is3D)
            {
                GlStateManager.pushMatrix();

                if(k > 0)
                {
                    float offsetX = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float offsetY = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float offsetZ = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(this.shouldSpreadItems() ? offsetX : 0, this.shouldSpreadItems() ? offsetY : 0, offsetZ);
                }

                if(!MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Pre(entity, stack, partialTicks)))
                {
                    IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
                    this.renderItem.renderItem(stack, transformedModel);
                    MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Post(entity, stack, partialTicks));
                }

                GlStateManager.popMatrix();
            }
            else
            {
                GlStateManager.pushMatrix();

                if(k > 0)
                {
                    float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    GlStateManager.translate(f8, f10, 0.0F);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
                this.renderItem.renderItem(stack, transformedModel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.09375F);
            }
        }

        if(this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if(flag)
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }

        if(!this.renderOutlines)
        {
            this.renderName(entity, x, y, z);
        }
    }
}
