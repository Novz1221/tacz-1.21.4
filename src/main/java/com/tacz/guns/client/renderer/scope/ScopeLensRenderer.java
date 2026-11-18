package com.tacz.guns.client.renderer.scope;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tacz.guns.client.resource.pojo.display.attachment.ScopeLensConfig;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.function.Consumer;

/**
 * TODO(1.21.4): the shader based scope lens needs custom core shaders and framebuffer work that
 * changed completely in 1.21.2+. It is disabled for now; scopes render with their normal models.
 */
public final class ScopeLensRenderer {
    private ScopeLensRenderer() {}

    public static void registerShader() {}

    public static void beginFrame() {}

    public static boolean capture(float centerX, float centerY, float radius, int stencil, ScopeLensConfig config,
                                  Consumer<VertexConsumer> ocular,
                                  Consumer<MultiBufferSource> reticle) {
        return false;
    }

    public static void render() {}

    public static void close() {}
}
