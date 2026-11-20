package com.github.mcmodderanchor.simplebedrockmodel.v1.particle.runtime;

import org.joml.Matrix4f;

/** Stub: emitter partikel (no-op). */
public class ParticleEmitterInstance {
    public boolean isFinished() { return true; }
    public void tick(float partialTick) { }
    public void setWorldParticleScale(float scale) { }
    public void setEmitterTransform(Matrix4f emitterTransform, Matrix4f worldTransform) { }
    public void setFollowAnchor(boolean follow) { }
    public void setVariable(String name, float value) { }
}
