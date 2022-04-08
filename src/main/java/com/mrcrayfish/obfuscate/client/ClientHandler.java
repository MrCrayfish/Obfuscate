package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

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

    private ClientHandler() {}

    public void setup() {}

    public void updatePlayerData(int entityId, List<SyncedPlayerData.DataEntry<?>> entries)
    {
        World world = Minecraft.getInstance().level;
        if(world != null)
        {
            Entity entity = world.getEntity(entityId);
            if(entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entity;
                entries.forEach(entry -> SyncedPlayerData.instance().updateClientEntry(player, entry));
            }
        }
    }
}
