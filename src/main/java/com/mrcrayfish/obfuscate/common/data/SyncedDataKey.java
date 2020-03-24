package com.mrcrayfish.obfuscate.common.data;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class SyncedDataKey<T>
{
    private ResourceLocation key;
    private IDataSerializer<T> serializer;
    private Supplier<T> defaultValueSupplier;
    private int id;

    private SyncedDataKey(ResourceLocation key, IDataSerializer<T> serializer, Supplier<T> defaultValueSupplier)
    {
        this.key = key;
        this.serializer = serializer;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    public ResourceLocation getKey()
    {
        return key;
    }

    public IDataSerializer<T> getSerializer()
    {
        return serializer;
    }

    public Supplier<T> getDefaultValueSupplier()
    {
        return defaultValueSupplier;
    }

    public int getId()
    {
        return this.id;
    }

    void setId(int id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SyncedDataKey<?> that = (SyncedDataKey<?>) o;
        return Objects.equals(this.key, that.key);
    }

    @Override
    public int hashCode()
    {
        return this.key.hashCode();
    }

    public static <T> SyncedDataKey<T> create(ResourceLocation id, IDataSerializer<T> serializer, Supplier<T> defaultValue)
    {
        return new SyncedDataKey<>(id, serializer, defaultValue);
    }
}
