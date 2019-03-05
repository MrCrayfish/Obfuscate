package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Author: MrCrayfish
 */
public abstract class ModelPlayerEvent extends PlayerEvent
{
    private ModelPlayer modelPlayer;
    private float partialTicks;

    private ModelPlayerEvent(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
    {
        super(player);
        this.modelPlayer = modelPlayer;
        this.partialTicks = partialTicks;
    }

    public ModelPlayer getModelPlayer()
    {
        return modelPlayer;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    @Cancelable
    public static class SetupAngles extends ModelPlayerEvent
    {
        private SetupAngles(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
        {
            super(player, modelPlayer, partialTicks);
        }

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

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }

    @Cancelable
    public static class Render extends ModelPlayerEvent
    {
        private Render(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
        {
            super(player, modelPlayer, partialTicks);
        }

        public static class Pre extends Render
        {
            public Pre(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
        }

        public static class Post extends Render
        {
            public Post(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }
}
