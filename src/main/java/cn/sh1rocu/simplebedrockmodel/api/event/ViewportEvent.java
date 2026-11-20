package cn.sh1rocu.simplebedrockmodel.api.event;

import com.mojang.blaze3d.vertex.PoseStack;

/** Pengganti net.minecraftforge.client.event.ViewportEvent untuk Fabric. */
public class ViewportEvent {
    private ViewportEvent() {
    }

    public static class ComputeCameraAngles {
        private final PoseStack poseStack;
        private float yaw;
        private float pitch;
        private float roll;

        public ComputeCameraAngles(PoseStack poseStack, float yaw, float pitch, float roll) {
            this.poseStack = poseStack;
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }

        public PoseStack getPoseStack() { return poseStack; }
        public float getYaw() { return yaw; }
        public float getPitch() { return pitch; }
        public float getRoll() { return roll; }
        public void setYaw(float yaw) { this.yaw = yaw; }
        public void setPitch(float pitch) { this.pitch = pitch; }
        public void setRoll(float roll) { this.roll = roll; }
    }
}
