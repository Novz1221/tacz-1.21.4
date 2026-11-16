package com.tacz.guns.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/** Minimal replacement for Fabric's removed PacketType (used by the login handshake only). */
public final class PacketType<T> {
    private final ResourceLocation id;
    private final Function<FriendlyByteBuf, T> reader;

    private PacketType(ResourceLocation id, Function<FriendlyByteBuf, T> reader) {
        this.id = id;
        this.reader = reader;
    }

    public static <T> PacketType<T> create(ResourceLocation id, Function<FriendlyByteBuf, T> reader) {
        return new PacketType<>(id, reader);
    }

    public ResourceLocation getId() { return id; }
    public T read(FriendlyByteBuf buf) { return reader.apply(buf); }
}
