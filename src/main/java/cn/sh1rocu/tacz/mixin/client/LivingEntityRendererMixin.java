package cn.sh1rocu.tacz.mixin.client;

import cn.sh1rocu.tacz.api.event.RenderPlayerEvent;
import cn.sh1rocu.tacz.api.extension.IRenderStateEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
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

    // PlayerRenderer does not override render(); the player render events are fired from the shared implementation.
    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void tacz$onRenderPlayerPre(LivingEntityRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!((Object) this instanceof PlayerRenderer renderer)
                || !((Object) state instanceof IRenderStateEntity holder)
                || !(holder.tacz$getEntity() instanceof AbstractClientPlayer player)) {
            return;
        }
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        var event = new RenderPlayerEvent.Pre(player, renderer, partialTick, poseStack, buffer, packedLight);
        RenderPlayerEvent.PRE.invoker().post(event);
        if (event.isCanceled())
            ci.cancel();
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void tacz$onRenderPlayerPost(LivingEntityRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!((Object) this instanceof PlayerRenderer renderer)
                || !((Object) state instanceof IRenderStateEntity holder)
                || !(holder.tacz$getEntity() instanceof AbstractClientPlayer player)) {
            return;
        }
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        var event = new RenderPlayerEvent.Post(player, renderer, partialTick, poseStack, buffer, packedLight);
        RenderPlayerEvent.POST.invoker().post(event);
    }
}
