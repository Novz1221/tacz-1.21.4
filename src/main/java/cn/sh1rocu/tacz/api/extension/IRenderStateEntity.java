package cn.sh1rocu.tacz.api.extension;

import net.minecraft.world.entity.LivingEntity;

/** Duck interface on EntityRenderState: 1.21.2+ models only see render states, so keep the source entity here. */
public interface IRenderStateEntity {
    LivingEntity tacz$getEntity();

    void tacz$setEntity(LivingEntity entity);
}
