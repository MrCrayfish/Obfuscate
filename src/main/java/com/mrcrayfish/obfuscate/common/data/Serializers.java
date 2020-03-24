package com.mrcrayfish.obfuscate.common.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class Serializers
{
    public static final IDataSerializer<Boolean> BOOLEAN = new IDataSerializer<Boolean>()
    {
        @Override
        public void write(PacketBuffer buf, Boolean value)
        {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean read(PacketBuffer buf)
        {
            return buf.readBoolean();
        }
    };

    public static final IDataSerializer<Byte> BYTE = new IDataSerializer<Byte>()
    {
        @Override
        public void write(PacketBuffer buf, Byte value)
        {
            buf.writeByte(value);
        }

        @Override
        public Byte read(PacketBuffer buf)
        {
            return buf.readByte();
        }
    };

    public static final IDataSerializer<Short> SHORT = new IDataSerializer<Short>()
    {
        @Override
        public void write(PacketBuffer buf, Short value)
        {
            buf.writeShort(value);
        }

        @Override
        public Short read(PacketBuffer buf)
        {
            return buf.readShort();
        }
    };

    public static final IDataSerializer<Integer> INTEGER = new IDataSerializer<Integer>()
    {
        @Override
        public void write(PacketBuffer buf, Integer value)
        {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(PacketBuffer buf)
        {
            return buf.readVarInt();
        }
    };

    public static final IDataSerializer<Long> LONG = new IDataSerializer<Long>()
    {
        @Override
        public void write(PacketBuffer buf, Long value)
        {
            buf.writeLong(value);
        }

        @Override
        public Long read(PacketBuffer buf)
        {
            return buf.readLong();
        }
    };

    public static final IDataSerializer<Float> FLOAT = new IDataSerializer<Float>()
    {
        @Override
        public void write(PacketBuffer buf, Float value)
        {
            buf.writeFloat(value);
        }

        @Override
        public Float read(PacketBuffer buf)
        {
            return buf.readFloat();
        }
    };

    public static final IDataSerializer<Double> DOUBLE = new IDataSerializer<Double>()
    {
        @Override
        public void write(PacketBuffer buf, Double value)
        {
            buf.writeDouble(value);
        }

        @Override
        public Double read(PacketBuffer buf)
        {
            return buf.readDouble();
        }
    };

    public static final IDataSerializer<Character> CHARACTER = new IDataSerializer<Character>()
    {
        @Override
        public void write(PacketBuffer buf, Character value)
        {
            buf.writeChar(value);
        }

        @Override
        public Character read(PacketBuffer buf)
        {
            return buf.readChar();
        }
    };

    public static final IDataSerializer<String> STRING = new IDataSerializer<String>()
    {
        @Override
        public void write(PacketBuffer buf, String value)
        {
            buf.writeString(value);
        }

        @Override
        public String read(PacketBuffer buf)
        {
            return buf.readString();
        }
    };

    public static final IDataSerializer<CompoundNBT> TAG_COMPOUND = new IDataSerializer<CompoundNBT>()
    {
        @Override
        public void write(PacketBuffer buf, CompoundNBT value)
        {
            buf.writeCompoundTag(value);
        }

        @Override
        public CompoundNBT read(PacketBuffer buf)
        {
            return buf.readCompoundTag();
        }
    };

    public static final IDataSerializer<BlockPos> BLOCK_POS = new IDataSerializer<BlockPos>()
    {
        @Override
        public void write(PacketBuffer buf, BlockPos value)
        {
            buf.writeBlockPos(value);
        }

        @Override
        public BlockPos read(PacketBuffer buf)
        {
            return buf.readBlockPos();
        }
    };

    public static final IDataSerializer<UUID> UUID = new IDataSerializer<UUID>()
    {
        @Override
        public void write(PacketBuffer buf, UUID value)
        {
            buf.writeUniqueId(value);
        }

        @Override
        public UUID read(PacketBuffer buf)
        {
            return buf.readUniqueId();
        }
    };

    public static final IDataSerializer<ItemStack> ITEM_STACK = new IDataSerializer<ItemStack>()
    {
        @Override
        public void write(PacketBuffer buf, ItemStack value)
        {
            buf.writeItemStack(value);
        }

        @Override
        public ItemStack read(PacketBuffer buf)
        {
            return buf.readItemStack();
        }
    };

    public static final IDataSerializer<ResourceLocation> RESOURCE_LOCATION = new IDataSerializer<ResourceLocation>()
    {
        @Override
        public void write(PacketBuffer buf, ResourceLocation value)
        {
            buf.writeResourceLocation(value);
        }

        @Override
        public ResourceLocation read(PacketBuffer buf)
        {
            return buf.readResourceLocation();
        }
    };
}
