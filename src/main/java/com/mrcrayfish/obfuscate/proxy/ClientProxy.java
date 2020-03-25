package com.mrcrayfish.obfuscate.proxy;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.client.model.CustomModelPlayer;
import com.mrcrayfish.obfuscate.client.model.ModelBipedArmor;
import com.mrcrayfish.obfuscate.client.model.layer.LayerCustomHeldItem;
import com.mrcrayfish.obfuscate.client.renderer.entity.RenderCustomEntityItem;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, manager -> new RenderCustomEntityItem(manager, Minecraft.getMinecraft().getRenderItem()));
    }

    @Override
    public void init()
    {
        Render render = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(AbstractClientPlayer.class);
        Map<String, RenderPlayer> skinMap = render.getRenderManager().getSkinMap();
        this.patchPlayerRender(skinMap.get("default"), false);
        this.patchPlayerRender(skinMap.get("slim"), true);
    }

    private void patchPlayerRender(RenderPlayer player, boolean smallArms)
    {
        ModelBiped model = new CustomModelPlayer(0.0F, smallArms);
        List<LayerRenderer<EntityLivingBase>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, player, "field_177097_h");
        if(layers != null)
        {
            layers.removeIf(layerRenderer -> layerRenderer instanceof LayerHeldItem || layerRenderer instanceof LayerCustomHead);
            layers.add(new LayerCustomHeldItem(player));
            layers.add(new LayerCustomHead(model.bipedHead));
            layers.forEach(layerRenderer ->
            {
                if(layerRenderer instanceof LayerBipedArmor)
                {
                    this.patchArmor((LayerBipedArmor) layerRenderer, model);
                }
            });
        }
        ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, player, model, "field_77045_g");
    }

    private void patchArmor(LayerBipedArmor layerBipedArmor, ModelBiped source)
    {
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, layerBipedArmor, new ModelBipedArmor(source, 1.0F), "field_177186_d");
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, layerBipedArmor, new ModelBipedArmor(source, 0.5F), "field_177189_c");
    }

    @Override
    public void updatePlayerData(int entityId, List<SyncedPlayerData.DataEntry<?>> entries)
    {
        World world = Minecraft.getMinecraft().world;
        if(world != null)
        {
            Entity entity = world.getEntityByID(entityId);
            if(entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entity;
                entries.forEach(entry -> this.setSyncedValue(player, entry));
            }
        }
    }

    private <T> void setSyncedValue(EntityPlayer player, SyncedPlayerData.DataEntry<T> entry)
    {
        SyncedPlayerData.instance().set(player, entry.getKey(), entry.getValue());
    }
}
