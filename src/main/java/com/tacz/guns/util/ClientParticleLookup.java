package com.tacz.guns.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.stream.Stream;

/**
 * 1.21+: ParticleArgument needs a registry lookup. During resource loading at the title screen there is
 * no client level yet, so fall back to the built-in particle registry.
 */
public final class ClientParticleLookup {
    private static final HolderLookup.Provider BUILT_IN = HolderLookup.Provider.create(Stream.of(BuiltInRegistries.PARTICLE_TYPE));

    private ClientParticleLookup() {
    }

    public static HolderLookup.Provider get() {
        var level = Minecraft.getInstance().level;
        return level != null ? level.registryAccess() : BUILT_IN;
    }
}
