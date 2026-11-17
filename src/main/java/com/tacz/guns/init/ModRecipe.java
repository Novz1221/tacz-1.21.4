package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import com.tacz.guns.crafting.AmmoStorageUpgradeRecipe;
import com.tacz.guns.crafting.GunPegboardDyeRecipe;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.crafting.GunSmithTableSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.CustomRecipe;

public class ModRecipe {
    public static void init() {

    }

    public static final RecipeSerializer<GunPegboardDyeRecipe> GUN_PEGBOARD_DYE_RECIPE_SERIALIZER = registerSerializer("gun_pegboard_dye", new CustomRecipe.Serializer<>(GunPegboardDyeRecipe::new));

    public static RecipeSerializer<GunSmithTableRecipe> GUN_SMITH_TABLE_RECIPE_SERIALIZER = registerSerializer("gun_smith_table_crafting", new GunSmithTableSerializer());
    public static RecipeSerializer<AmmoStorageUpgradeRecipe> AMMO_STORAGE_UPGRADE_RECIPE_SERIALIZER = registerSerializer("ammo_storage_upgrade", new CustomRecipe.Serializer<>(category -> new AmmoStorageUpgradeRecipe(category, false)));
    public static RecipeSerializer<AmmoStorageUpgradeRecipe> AMMO_STORAGE_UPGRADE_DIAMOND_RECIPE_SERIALIZER = registerSerializer("ammo_storage_upgrade_diamond", new CustomRecipe.Serializer<>(category -> new AmmoStorageUpgradeRecipe(category, true)));
    public static RecipeType<GunSmithTableRecipe> GUN_SMITH_TABLE_CRAFTING = registerRecipe("gun_smith_table_crafting", new RecipeType<>() {
        @Override
        public String toString() {
            return GunMod.MOD_ID + ":gun_smith_table_crafting";
        }
    });

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String name, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name), serializer);
    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipe(String name, RecipeType<T> type) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name), type);
    }
}
