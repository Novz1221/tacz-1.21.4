package cn.sh1rocu.tacz.mixin.client;

import cn.sh1rocu.tacz.api.extension.IRenderStateEntity;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements IRenderStateEntity {
    @Unique
    private LivingEntity tacz$entity;

    @Override
    public LivingEntity tacz$getEntity() {
        return tacz$entity;
    }

    @Override
    public void tacz$setEntity(LivingEntity entity) {
        this.tacz$entity = entity;
    }
}
