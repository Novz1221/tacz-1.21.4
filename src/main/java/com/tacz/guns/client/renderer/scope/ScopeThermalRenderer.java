package com.tacz.guns.client.renderer.scope;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

/** TODO(1.21.4): thermal scope vision is disabled until the shader pipeline is ported. */
public final class ScopeThermalRenderer {
    private ScopeThermalRenderer() {}

    public static void registerShader() {}

    public static void beginFrame() {}

    public static boolean enabled() {
        return false;
    }

    public static MultiBufferSource wrap(MultiBufferSource source, Entity entity) {
        return source;
    }

    public static void onWorldStage(WorldRenderContext context) {}

    public static void attachSceneDepth(int type, int object, int level) {}

    public static boolean isReady(int width, int height) {
        return false;
    }

    public static int textureOr(int fallback, int width, int height) {
        return fallback;
    }

    public static void close() {}
}
