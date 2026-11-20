package com.github.mcmodderanchor.simplebedrockmodel.v1.client.handler;

import com.github.mcmodderanchor.simplebedrockmodel.v1.particle.firstperson.FirstPersonParticleSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;

/**
 * Stub fase 2a: hanya menyediakan API yang dipanggil TaCZ agar mod bisa dimuat.
 * Render tangan first-person lewat IFPGeoItemRenderer dan partikel belum aktif.
 */
public final class FirstPersonRenderHandler {
    private static final FirstPersonParticleSystem PARTICLE_SYSTEM = new FirstPersonParticleSystem();

    private FirstPersonRenderHandler() { }

    public static FirstPersonParticleSystem getParticleSystem() { return PARTICLE_SYSTEM; }

    public static void renderParticlesNow(InteractionHand hand, PoseStack poseStack, MultiBufferSource buffer,
                                          int light, float partialTick) { }

    public static void reset() { }
}
