package com.mrcrayfish.obfuscate.common.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;

/**
 * Author: MrCrayfish
 */
public interface IDataSerializer<T>
{
    void write(ByteBuf buf, T value);

    T read(ByteBuf buf);

    NBTBase write(T value);

    T read(NBTBase nbt);
}
