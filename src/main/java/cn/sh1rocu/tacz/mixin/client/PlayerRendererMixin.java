package cn.sh1rocu.tacz.mixin.client;

import cn.sh1rocu.tacz.api.event.RenderPlayerEvent;
import cn.sh1rocu.tacz.api.extension.IRenderStateEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void goety$onRenderPlayerPre(PlayerRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!((Object) state instanceof IRenderStateEntity holder) || !(holder.tacz$getEntity() instanceof AbstractClientPlayer player)) {
            return;
        }
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        var event = new RenderPlayerEvent.Pre(player, (PlayerRenderer) (Object) this, partialTick, poseStack, buffer, packedLight);
        RenderPlayerEvent.PRE.invoker().post(event);
        if (event.isCanceled())
            ci.cancel();
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void tacz$onRenderPlayerPost(PlayerRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!((Object) state instanceof IRenderStateEntity holder) || !(holder.tacz$getEntity() instanceof AbstractClientPlayer player)) {
            return;
        }
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        var event = new RenderPlayerEvent.Post(player, (PlayerRenderer) (Object) this, partialTick, poseStack, buffer, packedLight);
        RenderPlayerEvent.POST.invoker().post(event);
    }
}
