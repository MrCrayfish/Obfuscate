package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Author: MrCrayfish
 */
public abstract class PlayerModelEvent extends PlayerEvent
{
    private PlayerModel modelPlayer;
    private float partialTicks;

    private PlayerModelEvent(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
    {
        super(player);
        this.modelPlayer = modelPlayer;
        this.partialTicks = partialTicks;
    }

    public PlayerModel getModelPlayer()
    {
        return modelPlayer;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    @Cancelable
    public static class SetupAngles extends PlayerModelEvent
    {
        private SetupAngles(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
        {
            super(player, modelPlayer, partialTicks);
        }

        public static class Pre extends SetupAngles
        {
            public Pre(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
        }

        public static class Post extends SetupAngles
        {
            public Post(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
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
    public static class Render extends PlayerModelEvent
    {
        private Render(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
        {
            super(player, modelPlayer, partialTicks);
        }

        public static class Pre extends Render
        {
            public Pre(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
        }

        public static class Post extends Render
        {
            public Post(PlayerEntity player, PlayerModel modelPlayer, float partialTicks)
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
