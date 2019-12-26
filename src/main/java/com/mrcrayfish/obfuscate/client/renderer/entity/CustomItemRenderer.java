package com.mrcrayfish.obfuscate.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

    @Override
    public void func_225623_a_(ItemEntity entity, float p_225623_2_, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int p_225623_6_)
    {
        matrix.func_227860_a_();
        ItemStack stack = entity.getItem();
        int seed = stack.isEmpty() ? 187 : Item.getIdFromItem(stack.getItem()) + stack.getDamage();
        this.random.setSeed((long) seed);
        IBakedModel model = this.itemRenderer.getItemModelWithOverrides(stack, entity.world, null);
        boolean isGui3d = model.isGui3d();
        int modelCount = this.getModelCount(stack);
        float bobOffset = shouldBob() ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F : 0;
        float yScale = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY();
        matrix.func_227861_a_(0.0D, (double) (bobOffset + 0.25F * yScale), 0.0D);
        float rotation = ((float) entity.getAge() + partialTicks) / 20.0F + entity.hoverStart;
        matrix.func_227863_a_(Vector3f.field_229181_d_.func_229193_c_(rotation));
        if(!isGui3d)
        {
            float x = -0.0F * (float) (modelCount - 1) * 0.5F;
            float y = -0.0F * (float) (modelCount - 1) * 0.5F;
            float z = -0.09375F * (float) (modelCount - 1) * 0.5F;
            matrix.func_227861_a_((double) x, (double) y, (double) z);
        }

        for(int m = 0; m < modelCount; m++)
        {
            matrix.func_227860_a_(); //push
            if(m > 0)
            {
                if(isGui3d)
                {
                    float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    matrix.func_227861_a_(shouldSpreadItems() ? x : 0, shouldSpreadItems() ? y : 0, shouldSpreadItems() ? z : 0);
                }
                else
                {
                    float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    matrix.func_227861_a_(shouldSpreadItems() ? x : 0, shouldSpreadItems() ? y : 0, 0.0D);
                }
            }

            boolean cancelled = MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Pre(entity, stack, matrix, buffer, partialTicks));
            if(!cancelled)
            {
                this.itemRenderer.func_229111_a_(stack, ItemCameraTransforms.TransformType.GROUND, false, matrix, buffer, p_225623_6_, OverlayTexture.field_229196_a_, model);
                MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Entity.Post(entity, stack, matrix, buffer, partialTicks));
            }
            matrix.func_227865_b_(); //pop

            if(!isGui3d)
            {
                matrix.func_227861_a_(0.0, 0.0, 0.09375F);
            }
        }

        matrix.func_227865_b_();
        super.func_225623_a_(entity, p_225623_2_, partialTicks, matrix, buffer, p_225623_6_);
    }
}
