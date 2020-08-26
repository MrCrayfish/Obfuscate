package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.client.model.CustomBipedModel;
import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;
import com.mrcrayfish.obfuscate.client.util.PlayerModelUtil;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ClientHandler
{
    private static ClientHandler instance;

    public static ClientHandler instance()
    {
        if(instance == null)
        {
            instance = new ClientHandler();
        }
        return instance;
    }

    private ClientHandler()
    {
    }

    public void setup()
    {
        Obfuscate.LOGGER.info("Starting to patch player models...");
        PlayerModelUtil.modifyPlayerModel((renderer, layers, slim) -> {
            PlayerModel<AbstractClientPlayerEntity> model = new CustomPlayerModel(0.0F, slim);
            layers.removeIf(layer -> layer instanceof BipedArmorLayer);
            layers.add(new BipedArmorLayer<>(renderer, new CustomBipedModel<>(model, 0.5F), new CustomBipedModel<>(model, 1.0F)));
            ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, renderer, model, "field_77045_g");
            Obfuscate.LOGGER.info("Patched " + (slim ? "slim" : "default") + " model successfully");
        });
    }

    public void updatePlayerData(int entityId, List<SyncedPlayerData.DataEntry<?>> entries)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            Entity entity = world.getEntityByID(entityId);
            if(entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entity;
                entries.forEach(entry -> SyncedPlayerData.instance().updateClientEntry(player, entry));
            }
        }
    }
}
