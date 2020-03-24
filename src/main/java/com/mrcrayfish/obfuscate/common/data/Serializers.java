package com.mrcrayfish.obfuscate.common.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
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

        @Override
        public INBT write(Boolean value)
        {
            return ByteNBT.valueOf(value);
        }

        @Override
        public Boolean read(INBT nbt)
        {
            return ((ByteNBT) nbt).getByte() != 0;
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

        @Override
        public INBT write(Byte value)
        {
            return ByteNBT.valueOf(value);
        }

        @Override
        public Byte read(INBT nbt)
        {
            return ((ByteNBT) nbt).getByte();
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

        @Override
        public INBT write(Short value)
        {
            return ShortNBT.valueOf(value);
        }

        @Override
        public Short read(INBT nbt)
        {
            return ((ShortNBT) nbt).getShort();
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

        @Override
        public INBT write(Integer value)
        {
            return IntNBT.valueOf(value);
        }

        @Override
        public Integer read(INBT nbt)
        {
            return ((IntNBT) nbt).getInt();
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

        @Override
        public INBT write(Long value)
        {
            return LongNBT.valueOf(value);
        }

        @Override
        public Long read(INBT nbt)
        {
            return ((LongNBT) nbt).getLong();
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

        @Override
        public INBT write(Float value)
        {
            return FloatNBT.valueOf(value);
        }

        @Override
        public Float read(INBT nbt)
        {
            return ((FloatNBT) nbt).getFloat();
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

        @Override
        public INBT write(Double value)
        {
            return DoubleNBT.valueOf(value);
        }

        @Override
        public Double read(INBT nbt)
        {
            return ((DoubleNBT) nbt).getDouble();
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

        @Override
        public INBT write(Character value)
        {
            return IntNBT.valueOf(value);
        }

        @Override
        public Character read(INBT nbt)
        {
            return (char) ((IntNBT) nbt).getInt();
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

        @Override
        public INBT write(String value)
        {
            return StringNBT.valueOf(value);
        }

        @Override
        public String read(INBT nbt)
        {
            return nbt.getString();
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

        @Override
        public INBT write(CompoundNBT value)
        {
            return value;
        }

        @Override
        public CompoundNBT read(INBT nbt)
        {
            return (CompoundNBT) nbt;
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

        @Override
        public INBT write(BlockPos value)
        {
            return LongNBT.valueOf(value.toLong());
        }

        @Override
        public BlockPos read(INBT nbt)
        {
            return BlockPos.fromLong(((LongNBT) nbt).getLong());
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

        @Override
        public INBT write(UUID value)
        {
            CompoundNBT compound = new CompoundNBT();
            compound.putLong("Most", value.getMostSignificantBits());
            compound.putLong("Least", value.getLeastSignificantBits());
            return compound;
        }

        @Override
        public UUID read(INBT nbt)
        {
            CompoundNBT compound = new CompoundNBT();
            return new UUID(compound.getLong("Most"), compound.getLong("Least"));
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

        @Override
        public INBT write(ItemStack value)
        {
            return value.write(new CompoundNBT());
        }

        @Override
        public ItemStack read(INBT nbt)
        {
            return ItemStack.read((CompoundNBT) nbt);
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

        @Override
        public INBT write(ResourceLocation value)
        {
            return StringNBT.valueOf(value.toString());
        }

        @Override
        public ResourceLocation read(INBT nbt)
        {
            return ResourceLocation.tryCreate(nbt.getString());
        }
    };
}
