package com.tacz.guns.util.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tacz.guns.config.common.AmmoConfig;
import com.tacz.guns.util.EntityUtil;
import com.tacz.guns.util.HitboxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 1.21.2+ made {@link Explosion} an interface (the vanilla implementation is ServerExplosion),
 * so this class implements it directly and keeps the custom bullet-explosion logic.
 */
public class ProjectileExplosion implements Explosion {
    private static final ExplosionDamageCalculator DEFAULT_CONTEXT = new ExplosionDamageCalculator();
    private final ServerLevel level;
    private final Vec3 center;
    private final float power;
    private final float radius;
    private final boolean knockback;
    private final boolean fire;
    private final Entity owner;
    private final Entity exploder;
    private final DamageSource damageSource;
    private final BlockInteraction mode;
    private final ExplosionDamageCalculator damageCalculator;
    private final List<BlockPos> toBlow = Lists.newArrayList();
    private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

    public ProjectileExplosion(ServerLevel level, Entity owner, Entity exploder, @Nullable DamageSource source, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float power, float radius, boolean knockback, Explosion.BlockInteraction mode) {
        this.level = level;
        this.center = new Vec3(x, y, z);
        this.power = power;
        this.radius = radius;
        this.owner = owner;
        this.exploder = exploder;
        this.knockback = knockback;
        this.fire = AmmoConfig.EXPLOSIVE_AMMO_FIRE.get();
        this.mode = mode;
        this.damageSource = source == null ? Explosion.getDefaultDamageSource(level, exploder) : source;
        this.damageCalculator = damageCalculator == null ? DEFAULT_CONTEXT : damageCalculator;
    }

    public List<BlockPos> getToBlow() {
        return toBlow;
    }

    public Map<Player, Vec3> getHitPlayers() {
        return hitPlayers;
    }

    @Override
    public ServerLevel level() {
        return level;
    }

    @Override
    public BlockInteraction getBlockInteraction() {
        return mode;
    }

    @Override
    public LivingEntity getIndirectSourceEntity() {
        return Explosion.getIndirectSourceEntity(exploder);
    }

    @Override
    public Entity getDirectSourceEntity() {
        return exploder;
    }

    @Override
    public float radius() {
        return radius;
    }

    @Override
    public Vec3 center() {
        return center;
    }

    @Override
    public boolean canTriggerBlocks() {
        return false;
    }

    @Override
    public boolean shouldAffectBlocklikeEntities() {
        return false;
    }

    public void explode() {
        // 防止直接调用时使用无效半径进行爆炸计算
        if (!Float.isFinite(this.radius) || this.radius <= 0) {
            return;
        }
        this.level.gameEvent(this.exploder, GameEvent.EXPLODE, BlockPos.containing(center));
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for (int x = 0; x < i; ++x) {
            for (int y = 0; y < i; ++y) {
                for (int z = 0; z < i; ++z) {
                    if (x == 0 || x == i - 1 || y == 0 || y == i - 1 || z == 0 || z == i - 1) {
                        double d0 = ((float) x / (i - 1) * 2.0F - 1.0F);
                        double d1 = ((float) y / (i - 1) * 2.0F - 1.0F);
                        double d2 = ((float) z / (i - 1) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double blockX = this.center.x;
                        double blockY = this.center.y;
                        double blockZ = this.center.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos pos = BlockPos.containing(blockX, blockY, blockZ);
                            BlockState blockState = this.level.getBlockState(pos);
                            FluidState fluidState = this.level.getFluidState(pos);
                            if (!this.level.isInWorldBounds(pos)) {
                                break;
                            }

                            Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, pos, blockState, fluidState);
                            if (optional.isPresent()) {
                                f -= (optional.get() + f1) * f1;
                            }

                            if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, pos, blockState, f)) {
                                set.add(pos);
                            }

                            blockX += d0 * (double) f1;
                            blockY += d1 * (double) f1;
                            blockZ += d2 * (double) f1;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(set);
        float radius = this.radius;
        int minX = Mth.floor(this.center.x - (double) radius - 1.0D);
        int maxX = Mth.floor(this.center.x + (double) radius + 1.0D);
        int minY = Mth.floor(this.center.y - (double) radius - 1.0D);
        int maxY = Mth.floor(this.center.y + (double) radius + 1.0D);
        int minZ = Mth.floor(this.center.z - (double) radius - 1.0D);
        int maxZ = Mth.floor(this.center.z + (double) radius + 1.0D);
        radius *= 2;
        List<Entity> entities = this.level.getEntities(this.exploder, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        Vec3 explosionPos = this.center;

        for (Entity entity : entities) {
            if (entity.ignoreExplosion(this) || EntityUtil.isBulletDamageIgnored(entity)) {
                continue;
            }

            AABB boundingBox = HitboxHelper.getFixedBoundingBox(entity, this.owner);
            BlockHitResult result;
            double strength;
            double deltaX;
            double deltaY;
            double deltaZ;
            double minDistance = radius;

            Vec3[] d = new Vec3[15];

            if (!(entity instanceof LivingEntity)) {
                strength = Math.sqrt(entity.distanceToSqr(explosionPos)) * 2 / radius;
                deltaX = entity.getX() - this.center.x;
                deltaY = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.center.y;
                deltaZ = entity.getZ() - this.center.z;
            } else {
                deltaX = (boundingBox.maxX + boundingBox.minX) / 2;
                deltaY = (boundingBox.maxY + boundingBox.minY) / 2;
                deltaZ = (boundingBox.maxZ + boundingBox.minZ) / 2;
                d[0] = new Vec3(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
                d[1] = new Vec3(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                d[2] = new Vec3(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                d[3] = new Vec3(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
                d[4] = new Vec3(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                d[5] = new Vec3(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
                d[6] = new Vec3(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                d[7] = new Vec3(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                d[8] = new Vec3(boundingBox.minX, deltaY, deltaZ);
                d[9] = new Vec3(boundingBox.maxX, deltaY, deltaZ);
                d[10] = new Vec3(deltaX, boundingBox.minY, deltaZ);
                d[11] = new Vec3(deltaX, boundingBox.maxY, deltaZ);
                d[12] = new Vec3(deltaX, deltaY, boundingBox.minZ);
                d[13] = new Vec3(deltaX, deltaY, boundingBox.maxZ);
                d[14] = new Vec3(deltaX, deltaY, deltaZ);
                for (int s = 0; s < 15; s++) {
                    result = BlockRayTrace.rayTraceBlocks(this.level, new ClipContext(explosionPos, d[s], ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity) null));
                    minDistance = (result.getType() != BlockHitResult.Type.BLOCK) ? Math.min(minDistance, explosionPos.distanceTo(d[s])) : minDistance;
                }
                strength = minDistance * 2 / radius;
                deltaX -= this.center.x;
                deltaY -= this.center.y;
                deltaZ -= this.center.z;
            }

            if (strength > 1.0D) {
                continue;
            }

            double distanceToExplosion = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            if (distanceToExplosion != 0.0D) {
                deltaX /= distanceToExplosion;
                deltaY /= distanceToExplosion;
                deltaZ /= distanceToExplosion;
            }

            double damage = 1.0D - strength;
            entity.hurtServer(this.level, this.damageSource, (float) damage * this.power);

            if (entity instanceof LivingEntity livingEntity) {
                damage = damage * (1.0D - livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
            }

            float multiplier = this.power * radius / 500;
            // 启用击退效果
            if (AmmoConfig.EXPLOSIVE_AMMO_KNOCK_BACK.get() && this.knockback) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(deltaX * damage * multiplier, deltaY * damage * multiplier, deltaZ * damage * multiplier));
                if (entity instanceof Player player) {
                    if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                        this.hitPlayers.put(player, new Vec3(deltaX * damage * multiplier, deltaY * damage * multiplier, deltaZ * damage * multiplier));
                    }
                }
            }
        }
    }

    /** Destroys the collected blocks (replaces the removed Explosion#finalizeExplosion). */
    public void finalizeExplosion() {
        if (this.mode == BlockInteraction.DESTROY) {
            for (BlockPos pos : this.toBlow) {
                BlockState state = this.level.getBlockState(pos);
                if (state.isAir()) {
                    continue;
                }
                Block block = state.getBlock();
                Block.dropResources(state, this.level, pos, state.hasBlockEntity() ? this.level.getBlockEntity(pos) : null, this.exploder, ItemStack.EMPTY);
                this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                block.wasExploded(this.level, pos, this);
            }
        }
        if (this.fire) {
            for (BlockPos pos : this.toBlow) {
                if (this.level.random.nextInt(3) == 0 && this.level.getBlockState(pos).isAir()
                        && this.level.getBlockState(pos.below()).isSolidRender()) {
                    this.level.setBlockAndUpdate(pos, BaseFireBlock.getState(this.level, pos));
                }
            }
        }
    }
}
