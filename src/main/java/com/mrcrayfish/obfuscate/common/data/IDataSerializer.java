package com.mrcrayfish.obfuscate.common.data;

import net.minecraft.network.PacketBuffer;

/**
 * Author: MrCrayfish
 */
public interface IDataSerializer<T>
{
    void write(PacketBuffer buf, T value);

    T read(PacketBuffer buf);
}
