package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Author: MrCrayfish
 */
public abstract class ModelPlayerEvent extends PlayerEvent
{
    private ModelPlayer modelPlayer;

    private ModelPlayerEvent(EntityPlayer player, ModelPlayer modelPlayer)
    {
        super(player);
        this.modelPlayer = modelPlayer;
    }

    public ModelPlayer getModelPlayer()
    {
        return modelPlayer;
    }

    public static class SetupAngles extends ModelPlayerEvent
    {
        private final float partialTicks;

        private SetupAngles(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
        {
            super(player, modelPlayer);
            this.partialTicks = partialTicks;
        }

        public float getPartialTicks()
        {
            return partialTicks;
        }

        @Cancelable
        public static class Pre extends SetupAngles
        {
            public Pre(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
        }

        public static class Post extends SetupAngles
        {
            public Post(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
        }
    }
}
