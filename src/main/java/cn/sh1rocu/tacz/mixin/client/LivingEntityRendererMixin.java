package cn.sh1rocu.tacz.mixin.client;

import cn.sh1rocu.tacz.api.extension.IRenderStateEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void tacz$captureEntity(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
        if ((Object) state instanceof IRenderStateEntity holder) {
            holder.tacz$setEntity(entity);
        }
    }
}
