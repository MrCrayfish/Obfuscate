package com.mrcrayfish.obfuscate.common.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Author: MrCrayfish
 */
public class EntityLivingInitEvent extends LivingEvent
{
    public EntityLivingInitEvent(EntityLivingBase entity)
    {
        super(entity);
    }
}
