package com.mrcrayfish.obfuscate.common.data;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;

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
    private boolean save;
    private boolean persistent;
    private boolean syncToClient;
    private boolean syncToAllPlayers;
    private int id;

    private SyncedDataKey(ResourceLocation key, IDataSerializer<T> serializer, Supplier<T> defaultValueSupplier)
    {
        this.key = key;
        this.serializer = serializer;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    public ResourceLocation getKey()
    {
        return this.key;
    }

    public IDataSerializer<T> getSerializer()
    {
        return this.serializer;
    }

    public Supplier<T> getDefaultValueSupplier()
    {
        return this.defaultValueSupplier;
    }

    public boolean shouldSave()
    {
        return this.save;
    }

    public boolean isPersistent()
    {
        return this.persistent;
    }

    public boolean shouldSyncToClient()
    {
        return this.syncToClient;
    }

    public boolean shouldSyncToAllPlayers()
    {
        return this.syncToAllPlayers;
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

    public static <T> SyncedDataKey.Builder<T> builder(IDataSerializer<T> serializer)
    {
        return new Builder<>(serializer);
    }

    public static class Builder<T>
    {
        private ResourceLocation id;
        private IDataSerializer<T> serializer;
        private Supplier<T> defaultValueSupplier;
        private boolean save = false;
        private boolean persistent = true;
        private boolean syncToClient = true;
        private boolean syncToAllPlayers = true;

        private Builder(IDataSerializer<T> serializer)
        {
            this.serializer = serializer;
        }

        public SyncedDataKey<T> build()
        {
            Validate.notNull(this.id, "Missing 'id' when building synced data key");
            Validate.notNull(this.serializer, "Missing 'serializer' when building synced data key");
            Validate.notNull(this.defaultValueSupplier, "Missing 'defaultValueSupplier' when building synced data key");
            SyncedDataKey<T> key = new SyncedDataKey<>(this.id, this.serializer, this.defaultValueSupplier);
            key.save = this.save;
            key.persistent = this.persistent;
            key.syncToClient = this.syncToClient;
            key.syncToAllPlayers = this.syncToAllPlayers;
            return key;
        }

        /**
         * Sets the id for the synced key. This is a required property.
         */
        public Builder<T> id(ResourceLocation id)
        {
            this.id = id;
            return this;
        }

        /**
         * Sets the id for the synced key using a String. This is a required property.
         */
        public Builder<T> id(String id)
        {
            this.id = new ResourceLocation(id);
            return this;
        }

        /**
         * Sets the id for the synced key using a String. This is a required property.
         *
         * Please use {@link #id(String)} instead.
         */
        @Deprecated
        public Builder<T> key(String key)
        {
            return id(key);
        }

        /**
         * Sets the default value supplier for the synced key. This is a required property.
         */
        public Builder<T> defaultValueSupplier(Supplier<T> defaultValueSupplier)
        {
            this.defaultValueSupplier = defaultValueSupplier;
            return this;
        }

        /**
         * Saves this synced key to the players file. This means that the data will persist even if
         * the player reloads a world or joins back into the server.
         */
        public Builder<T> saveToFile()
        {
            this.save = true;
            return this;
        }

        /**
         * Stops this synced key from transferring over when a player dies and basically resets the
         * data back to the result of the default value supplier.
         */
        public Builder<T> resetOnDeath()
        {
            this.persistent = false;
            return this;
        }

        /**
         * Stops the synced key from syncing at all. This is only useful if the data only needs to
         * be available on the server. This does contradict the whole idea of this system but
         * creates a quick way to store data that is bound to a player.
         */
        public Builder<T> doNotSync()
        {
            this.syncToClient = false;
            return this;
        }

        /**
         * Restricts this synced key from syncing to everyone who is tracking the player and only to
         * the player which data was changed.
         */
        public Builder<T> restrictSync()
        {
            this.syncToAllPlayers = false;
            return this;
        }
    }
}
