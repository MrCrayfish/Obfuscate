package com.mrcrayfish.obfuscate.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class CustomItemRenderer extends ItemRenderer
{
    private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
    private final Random random = new Random();

    public CustomItemRenderer(EntityRendererManager manager, net.minecraft.client.renderer.ItemRenderer renderer)
    {
        super(manager, renderer);
        this.itemRenderer = renderer;
    }

    private int transformModelCount(ItemEntity entity, double x, double y, double z, float partialTicks, IBakedModel model)
    {
        ItemStack stack = entity.getItem();
        if(stack.isEmpty())
        {
            return 0;
        }

        float f1 = shouldBob() ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F : 0;
        float f2 = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY();
        GlStateManager.translatef((float) x, (float) y + f1 + 0.25F * f2, (float) z);

        if(model.isGui3d() || this.renderManager.options != null)
        {
            float f3 = (((float) entity.getAge() + partialTicks) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI);
            GlStateManager.rotatef(f3, 0.0F, 1.0F, 0.0F);
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        return this.getModelCount(stack);
    }

    @Override
    public void doRender(ItemEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ItemStack itemstack = entity.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getDamage();
        this.random.setSeed((long) i);
        boolean flag = false;

        if(this.bindEntityTexture(entity))
        {
            this.renderManager.textureManager.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, null);
        int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
        boolean flag1 = ibakedmodel.isGui3d();

        if(!flag1)
        {
            float f3 = -0.0F * (float) (j - 1) * 0.5F;
            float f4 = -0.0F * (float) (j - 1) * 0.5F;
            float f5 = -0.09375F * (float) (j - 1) * 0.5F;
            GlStateManager.translatef(f3, f4, f5);
        }

        if(this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        for(int k = 0; k < j; ++k)
        {
            if(flag1)
            {
                GlStateManager.pushMatrix();

                if(k > 0)
                {
                    float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translatef(shouldSpreadItems() ? f7 : 0, shouldSpreadItems() ? f9 : 0, f6);
                }

                GlStateManager.pushMatrix();
                boolean cancelled = MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Pre(entity, itemstack, partialTicks));
                GlStateManager.popMatrix();

                if(!cancelled)
                {
                    GlStateManager.pushMatrix();
                    IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                    this.itemRenderer.renderItem(itemstack, transformedModel);
                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Post(entity, itemstack, partialTicks));
                    GlStateManager.popMatrix();
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
                    GlStateManager.translatef(f8, f10, 0.0F);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.itemRenderer.renderItem(itemstack, transformedModel);
                GlStateManager.popMatrix();
                GlStateManager.translatef(0.0F, 0.0F, 0.09375F);
            }
        }

        if(this.renderOutlines)
        {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if(flag)
        {
            this.renderManager.textureManager.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }
    }
}
