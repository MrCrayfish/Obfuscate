package com.mrcrayfish.obfuscate.common.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class Serializers
{
    public static final IDataSerializer<Boolean> BOOLEAN = new IDataSerializer<Boolean>()
    {
        @Override
        public void write(ByteBuf buf, Boolean value)
        {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean read(ByteBuf buf)
        {
            return buf.readBoolean();
        }

        @Override
        public NBTBase write(Boolean value)
        {
            return new NBTTagByte((byte) (value ? 1 : 0));
        }

        @Override
        public Boolean read(NBTBase nbt)
        {
            return ((NBTTagByte) nbt).getByte() != 0;
        }
    };

    public static final IDataSerializer<Byte> BYTE = new IDataSerializer<Byte>()
    {
        @Override
        public void write(ByteBuf buf, Byte value)
        {
            buf.writeByte(value);
        }

        @Override
        public Byte read(ByteBuf buf)
        {
            return buf.readByte();
        }

        @Override
        public NBTBase write(Byte value)
        {
            return new NBTTagByte(value);
        }

        @Override
        public Byte read(NBTBase nbt)
        {
            return ((NBTTagByte) nbt).getByte();
        }
    };

    public static final IDataSerializer<Short> SHORT = new IDataSerializer<Short>()
    {
        @Override
        public void write(ByteBuf buf, Short value)
        {
            buf.writeShort(value);
        }

        @Override
        public Short read(ByteBuf buf)
        {
            return buf.readShort();
        }

        @Override
        public NBTBase write(Short value)
        {
            return new NBTTagShort(value);
        }

        @Override
        public Short read(NBTBase nbt)
        {
            return ((NBTTagShort) nbt).getShort();
        }
    };

    public static final IDataSerializer<Integer> INTEGER = new IDataSerializer<Integer>()
    {
        @Override
        public void write(ByteBuf buf, Integer value)
        {
            ByteBufUtils.writeVarInt(buf, value, 4);
        }

        @Override
        public Integer read(ByteBuf buf)
        {
            return ByteBufUtils.readVarInt(buf, 4);
        }

        @Override
        public NBTBase write(Integer value)
        {
            return new NBTTagInt(value);
        }

        @Override
        public Integer read(NBTBase nbt)
        {
            return ((NBTTagInt) nbt).getInt();
        }
    };

    public static final IDataSerializer<Long> LONG = new IDataSerializer<Long>()
    {
        @Override
        public void write(ByteBuf buf, Long value)
        {
            buf.writeLong(value);
        }

        @Override
        public Long read(ByteBuf buf)
        {
            return buf.readLong();
        }

        @Override
        public NBTBase write(Long value)
        {
            return new NBTTagLong(value);
        }

        @Override
        public Long read(NBTBase nbt)
        {
            return ((NBTTagLong) nbt).getLong();
        }
    };

    public static final IDataSerializer<Float> FLOAT = new IDataSerializer<Float>()
    {
        @Override
        public void write(ByteBuf buf, Float value)
        {
            buf.writeFloat(value);
        }

        @Override
        public Float read(ByteBuf buf)
        {
            return buf.readFloat();
        }

        @Override
        public NBTBase write(Float value)
        {
            return new NBTTagFloat(value);
        }

        @Override
        public Float read(NBTBase nbt)
        {
            return ((NBTTagFloat) nbt).getFloat();
        }
    };

    public static final IDataSerializer<Double> DOUBLE = new IDataSerializer<Double>()
    {
        @Override
        public void write(ByteBuf buf, Double value)
        {
            buf.writeDouble(value);
        }

        @Override
        public Double read(ByteBuf buf)
        {
            return buf.readDouble();
        }

        @Override
        public NBTBase write(Double value)
        {
            return new NBTTagDouble(value);
        }

        @Override
        public Double read(NBTBase nbt)
        {
            return ((NBTTagDouble) nbt).getDouble();
        }
    };

    public static final IDataSerializer<Character> CHARACTER = new IDataSerializer<Character>()
    {
        @Override
        public void write(ByteBuf buf, Character value)
        {
            buf.writeChar(value);
        }

        @Override
        public Character read(ByteBuf buf)
        {
            return buf.readChar();
        }

        @Override
        public NBTBase write(Character value)
        {
            return new NBTTagInt(value);
        }

        @Override
        public Character read(NBTBase nbt)
        {
            return (char) ((NBTTagInt) nbt).getInt();
        }
    };

    public static final IDataSerializer<String> STRING = new IDataSerializer<String>()
    {
        @Override
        public void write(ByteBuf buf, String value)
        {
            ByteBufUtils.writeUTF8String(buf, value);
        }

        @Override
        public String read(ByteBuf buf)
        {
            return ByteBufUtils.readUTF8String(buf);
        }

        @Override
        public NBTBase write(String value)
        {
            return new NBTTagString(value);
        }

        @Override
        public String read(NBTBase nbt)
        {
            return ((NBTTagString) nbt).getString();
        }
    };

    public static final IDataSerializer<NBTTagCompound> TAG_COMPOUND = new IDataSerializer<NBTTagCompound>()
    {
        @Override
        public void write(ByteBuf buf, NBTTagCompound value)
        {
            ByteBufUtils.writeTag(buf, value);
        }

        @Override
        public NBTTagCompound read(ByteBuf buf)
        {
            return ByteBufUtils.readTag(buf);
        }

        @Override
        public NBTBase write(NBTTagCompound value)
        {
            return value;
        }

        @Override
        public NBTTagCompound read(NBTBase nbt)
        {
            return (NBTTagCompound) nbt;
        }
    };

    public static final IDataSerializer<BlockPos> BLOCK_POS = new IDataSerializer<BlockPos>()
    {
        @Override
        public void write(ByteBuf buf, BlockPos value)
        {
            buf.writeLong(value.toLong());
        }

        @Override
        public BlockPos read(ByteBuf buf)
        {
            return BlockPos.fromLong(buf.readLong());
        }

        @Override
        public NBTBase write(BlockPos value)
        {
            return new NBTTagLong(value.toLong());
        }

        @Override
        public BlockPos read(NBTBase nbt)
        {
            return BlockPos.fromLong(((NBTTagLong) nbt).getLong());
        }
    };

    public static final IDataSerializer<UUID> UUID = new IDataSerializer<UUID>()
    {
        @Override
        public void write(ByteBuf buf, UUID value)
        {
            buf.writeLong(value.getMostSignificantBits());
            buf.writeLong(value.getLeastSignificantBits());
        }

        @Override
        public UUID read(ByteBuf buf)
        {
            return new UUID(buf.readLong(), buf.readLong());
        }

        @Override
        public NBTBase write(UUID value)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setLong("Most", value.getMostSignificantBits());
            compound.setLong("Least", value.getLeastSignificantBits());
            return compound;
        }

        @Override
        public UUID read(NBTBase nbt)
        {
            NBTTagCompound compound = new NBTTagCompound();
            return new UUID(compound.getLong("Most"), compound.getLong("Least"));
        }
    };

    public static final IDataSerializer<ItemStack> ITEM_STACK = new IDataSerializer<ItemStack>()
    {
        @Override
        public void write(ByteBuf buf, ItemStack value)
        {
            ByteBufUtils.writeItemStack(buf, value);
        }

        @Override
        public ItemStack read(ByteBuf buf)
        {
            return ByteBufUtils.readItemStack(buf);
        }

        @Override
        public NBTBase write(ItemStack value)
        {
            return value.writeToNBT(new NBTTagCompound());
        }

        @Override
        public ItemStack read(NBTBase nbt)
        {
            return new ItemStack((NBTTagCompound) nbt);
        }
    };

    public static final IDataSerializer<ResourceLocation> RESOURCE_LOCATION = new IDataSerializer<ResourceLocation>()
    {
        @Override
        public void write(ByteBuf buf, ResourceLocation value)
        {
            ByteBufUtils.writeUTF8String(buf, value.toString());
        }

        @Override
        public ResourceLocation read(ByteBuf buf)
        {
            return new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        }

        @Override
        public NBTBase write(ResourceLocation value)
        {
            return new NBTTagString(value.toString());
        }

        @Override
        public ResourceLocation read(NBTBase nbt)
        {
            return new ResourceLocation(((NBTTagString) nbt).getString());
        }
    };
}
