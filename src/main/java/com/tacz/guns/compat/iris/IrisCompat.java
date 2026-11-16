package com.tacz.guns.compat.iris;

import net.minecraft.client.renderer.MultiBufferSource;

import javax.annotation.Nullable;

/** Stub: Iris integration is disabled in the 1.21.4 port. */
public final class IrisCompat {
    public static void initCompat() {}
    public static boolean isRenderShadow() { return false; }
    public static boolean isUsingRenderPack() { return false; }
    public static boolean endBatch(MultiBufferSource bufferSource) { return false; }
    @Nullable
    public static MultiBufferSource getHandBufferSource() { return null; }
}
