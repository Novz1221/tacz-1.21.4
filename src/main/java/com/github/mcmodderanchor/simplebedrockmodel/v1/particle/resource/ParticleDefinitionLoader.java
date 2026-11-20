package com.github.mcmodderanchor.simplebedrockmodel.v1.particle.resource;

import com.github.mcmodderanchor.simplebedrockmodel.v1.particle.data.ParticleEffectDefinition;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/** Stub: loader definisi partikel. Selalu null sampai sistem partikel di-port. */
public class ParticleDefinitionLoader {
    private static final ParticleDefinitionLoader INSTANCE = new ParticleDefinitionLoader();

    public static ParticleDefinitionLoader getInstance() { return INSTANCE; }

    @Nullable
    public ParticleEffectDefinition getDefinition(ResourceLocation id) { return null; }
}
