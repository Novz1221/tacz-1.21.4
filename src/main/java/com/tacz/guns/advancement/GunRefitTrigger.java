package com.tacz.guns.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class GunRefitTrigger extends SimpleCriterionTrigger<GunRefitTrigger.TriggerInstance> {
    public static final GunRefitTrigger INSTANCE = new GunRefitTrigger();
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("tacz", "gun_refit");

    public static void register() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, ID, INSTANCE);
    }

    public void trigger(ServerPlayer player, ItemStack before, ItemStack after) {
        if (!ItemStack.matches(before, after)) {
            this.trigger(player, instance -> true);
        }
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
        ).apply(instance, TriggerInstance::new));
    }
}
