package com.tacz.guns.compat.playeranimator;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.resource.GunDisplayInstance;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

import java.io.File;
import java.util.function.Consumer;
import java.util.zip.ZipFile;

/** Stub: Player Animator integration is disabled in the 1.21.4 port. */
public class PlayerAnimatorCompat {
    public static ResourceLocation LOWER_ANIMATION = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "lower_animation");
    public static ResourceLocation LOOP_UPPER_ANIMATION = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "loop_upper_animation");
    public static ResourceLocation ONCE_UPPER_ANIMATION = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "once_upper_animation");
    public static ResourceLocation ROTATION_ANIMATION = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "rotation");

    public static void init() {}
    public static boolean loadAnimationFromZip(ZipFile zipFile, String zipPath) { return false; }
    public static void loadAnimationFromFile(File file) {}
    public static void clearAllAnimationCache() {}
    public static boolean hasPlayerAnimator3rd(LivingEntity livingEntity, GunDisplayInstance display) { return false; }
    public static boolean canPlayAnimation(LivingEntity entity) {
        return IGun.getIGunOrNull(entity.getMainHandItem()) != null
                && entity.getPose() != Pose.SLEEPING && !entity.onClimbable()
                && !entity.isSwimming() && entity.getPose() != Pose.FALL_FLYING;
    }
    public static void stopAllAnimation(LivingEntity livingEntity) {}
    public static void stopAllAnimation(LivingEntity livingEntity, int fadeTime) {}
    public static void playAnimation(LivingEntity livingEntity, GunDisplayInstance display, float limbSwingAmount) {}
    public static boolean isInstalled() { return false; }
    public static Runnable captureModelState(PlayerModel<?> model) { return () -> {}; }
    public static void registerReloadListener(Consumer<IdentifiableResourceReloadListener> register) {}
}
