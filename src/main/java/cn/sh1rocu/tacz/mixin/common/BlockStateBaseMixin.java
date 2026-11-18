package cn.sh1rocu.tacz.mixin.common;

import cn.sh1rocu.tacz.api.extension.IBlockExtension;
import cn.sh1rocu.tacz.api.extension.IPistonPushReaction;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Shadow
    public abstract Block getBlock();

    @Shadow
    protected abstract BlockState asState();

    @ModifyReturnValue(method = "getPistonPushReaction", at = @At("RETURN"))
    private PushReaction tacz$getPistonPushReaction(PushReaction original) {
        if (getBlock() instanceof IPistonPushReaction custom) {
            PushReaction reaction = custom.getPistonPushReaction(asState());
            if (reaction != null) return reaction;
        }
        return original;
    }

    // 1.21.2+: Explosion is an interface and finalizeExplosion is gone; block hits go through onExplosionHit.
    @Inject(method = "onExplosionHit", at = @At("HEAD"))
    private void tacz$onBlockExploded(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo ci) {
        if (getBlock() instanceof IBlockExtension block) {
            block.tacz$onBlockExploded(asState(), level, pos, explosion);
        }
    }
}
