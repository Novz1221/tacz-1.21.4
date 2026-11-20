package com.tacz.guns.mixin.client;

import cn.sh1rocu.tacz.api.extension.IRenderStateEntity;
import com.tacz.guns.client.animation.third.InnerThirdPersonManager;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Shadow
    @Final
    public ModelPart head;
    @Shadow
    @Final
    public ModelPart body;
    @Shadow
    @Final
    public ModelPart leftArm;
    @Shadow
    @Final
    public ModelPart rightArm;

    // 1.21.2+: models only receive a render state; the entity is carried by IRenderStateEntity.
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "TAIL"))
    private void setRotationAnglesHead(T state, CallbackInfo ci) {
        if ((Object) state instanceof IRenderStateEntity holder) {
            LivingEntity entity = holder.tacz$getEntity();
            if (entity != null) {
                InnerThirdPersonManager.setRotationAnglesHead(entity, rightArm, leftArm, body, head, state.walkAnimationSpeed);
            }
        }
    }
}
