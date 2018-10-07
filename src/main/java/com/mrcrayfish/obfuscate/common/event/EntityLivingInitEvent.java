package com.mrcrayfish.obfuscate.common.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Deprecated as of 0.2.6. This event will no longer work and modders should now use
 * the EntityConstructing event which is in Forge by default.
 * Author: MrCrayfish
 */
@Deprecated
public class EntityLivingInitEvent extends LivingEvent
{
    public EntityLivingInitEvent(EntityLivingBase entity)
    {
        super(entity);
    }
}
