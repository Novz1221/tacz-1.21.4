package cn.sh1rocu.simplebedrockmodel.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;

import java.util.function.Consumer;

/** Pengganti net.minecraftforge.client.event.ViewportEvent untuk Fabric. */
public class ViewportEvent {
    /** Dipanggil saat sudut kamera dihitung (roll/pitch/yaw). */
    public static final Event<Consumer<ComputeCameraAngles>> CAMERA = EventFactory.createArrayBacked(Consumer.class,
            listeners -> event -> {
                for (Consumer<ComputeCameraAngles> l : listeners) {
                    l.accept(event);
                }
            });

    /** Dipanggil saat FOV dihitung. */
    public static final Event<Consumer<ComputeFov>> FOV = EventFactory.createArrayBacked(Consumer.class,
            listeners -> event -> {
                for (Consumer<ComputeFov> l : listeners) {
                    l.accept(event);
                }
            });

    private ViewportEvent() {
    }

    public static class ComputeCameraAngles {
        private final PoseStack poseStack;
        private final GameRenderer renderer;
        private final Camera camera;
        private final double partialTick;
        private float yaw;
        private float pitch;
        private float roll;

        public ComputeCameraAngles(PoseStack poseStack, float yaw, float pitch, float roll) {
            this(poseStack, null, null, 0, yaw, pitch, roll);
        }

        public ComputeCameraAngles(PoseStack poseStack, GameRenderer renderer, Camera camera, double partialTick,
                                   float yaw, float pitch, float roll) {
            this.poseStack = poseStack;
            this.renderer = renderer;
            this.camera = camera;
            this.partialTick = partialTick;
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }

        public PoseStack getPoseStack() { return poseStack; }
        public GameRenderer getRenderer() { return renderer; }
        public Camera getCamera() { return camera; }
        public double getPartialTick() { return partialTick; }
        public float getYaw() { return yaw; }
        public float getPitch() { return pitch; }
        public float getRoll() { return roll; }
        public void setYaw(float yaw) { this.yaw = yaw; }
        public void setPitch(float pitch) { this.pitch = pitch; }
        public void setRoll(float roll) { this.roll = roll; }
    }

    public static class ComputeFov {
        private final GameRenderer renderer;
        private final Camera camera;
        private final double partialTick;
        private final boolean usedConfiguredFov;
        private double fov;

        public ComputeFov(GameRenderer renderer, Camera camera, double partialTick, double fov, boolean usedConfiguredFov) {
            this.renderer = renderer;
            this.camera = camera;
            this.partialTick = partialTick;
            this.fov = fov;
            this.usedConfiguredFov = usedConfiguredFov;
        }

        public GameRenderer getRenderer() { return renderer; }
        public Camera getCamera() { return camera; }
        public double getPartialTick() { return partialTick; }
        public boolean usedConfiguredFov() { return usedConfiguredFov; }
        public double getFOV() { return fov; }
        public double getFov() { return fov; }
        public void setFOV(double fov) { this.fov = fov; }
        public void setFov(double fov) { this.fov = fov; }
    }
}
