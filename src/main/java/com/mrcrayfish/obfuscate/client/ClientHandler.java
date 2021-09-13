package com.mrcrayfish.obfuscate.client;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
        Level level = Minecraft.getInstance().level;
        if(level != null)
        {
            Entity entity = level.getEntity(entityId);
            if(entity instanceof Player player)
            {
                entries.forEach(entry -> SyncedPlayerData.instance().updateClientEntry(player, entry));
            }
        }
    }
}
