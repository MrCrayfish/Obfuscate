package com.mrcrayfish.obfuscate.common.data;

import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;

/**
 * Author: MrCrayfish
 */
public interface IDataSerializer<T>
{
    void write(PacketBuffer buf, T value);

    T read(PacketBuffer buf);

    INBT write(T value);

    T read(INBT nbt);
}
