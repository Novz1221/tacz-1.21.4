package com.tacz.guns.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class TargetHitTrigger extends SimpleCriterionTrigger<TargetHitTrigger.TriggerInstance> {
    public static final TargetHitTrigger INSTANCE = new TargetHitTrigger();
    static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("tacz", "target_hit");

    public static void register() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, ID, INSTANCE);
    }

    public void trigger(ServerPlayer player, double distance) {
        this.trigger(player, instance -> instance.matches(distance));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Doubles distance) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                MinMaxBounds.Doubles.CODEC.optionalFieldOf("distance", MinMaxBounds.Doubles.ANY).forGetter(TriggerInstance::distance)
        ).apply(instance, TriggerInstance::new));

        public boolean matches(double distance) {
            return this.distance.matches(distance);
        }
    }
}
