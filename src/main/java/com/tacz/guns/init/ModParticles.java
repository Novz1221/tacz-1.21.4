package com.tacz.guns.init;

import com.mojang.serialization.Codec;
import com.tacz.guns.GunMod;
import com.tacz.guns.particles.BulletHoleOption;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModParticles {
    public static void init() {

    }

    public static final ParticleType<BulletHoleOption> BULLET_HOLE = register("bullet_hole", new ModParticleType<>(false, BulletHoleOption.CODEC, BulletHoleOption.STREAM_CODEC));

    private static <T extends ParticleOptions> ParticleType<T> register(String name, ParticleType<T> type) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name), type);
    }

    private static class ModParticleType<T extends ParticleOptions> extends ParticleType<T> {
        private final com.mojang.serialization.MapCodec<T> codec;
        private final net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec;

        public ModParticleType(boolean overrideLimiter, com.mojang.serialization.MapCodec<T> codec,
                               net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec) {
            super(overrideLimiter);
            this.codec = codec;
            this.streamCodec = streamCodec;
        }

        @Override
        public @NotNull com.mojang.serialization.MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public @NotNull net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }
    }
}
