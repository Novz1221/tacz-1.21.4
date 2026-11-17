package com.tacz.guns.crafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Since 1.21.2 the client no longer receives recipes from the vanilla RecipeManager, so the
 * gun smith table recipes are looked up on the server and synced to the client by a custom packet.
 */
public final class GunSmithRecipes {
    private static volatile Map<ResourceLocation, GunSmithTableRecipe> CLIENT = Map.of();

    private GunSmithRecipes() {}

    public static void setClient(Map<ResourceLocation, GunSmithTableRecipe> recipes) {
        CLIENT = recipes;
    }

    @Nullable
    public static GunSmithTableRecipe find(@Nullable Level level, ResourceLocation id) {
        if (level == null) {
            return null;
        }
        if (level.isClientSide()) {
            return CLIENT.get(id);
        }
        MinecraftServer server = level.getServer();
        if (server == null) {
            return null;
        }
        Optional<RecipeHolder<?>> holder = server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id));
        if (holder.isPresent()) {
            Recipe<?> recipe = holder.get().value();
            if (recipe instanceof GunSmithTableRecipe smithRecipe) {
                smithRecipe.setId(id);
                return smithRecipe;
            }
        }
        return null;
    }

    public static List<GunSmithTableRecipe> all(@Nullable Level level) {
        if (level == null) {
            return List.of();
        }
        if (level.isClientSide()) {
            return new ArrayList<>(CLIENT.values());
        }
        MinecraftServer server = level.getServer();
        return server == null ? List.of() : serverAll(server.getRecipeManager());
    }

    public static List<GunSmithTableRecipe> serverAll(RecipeManager recipeManager) {
        List<GunSmithTableRecipe> list = new ArrayList<>();
        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
            if (holder.value() instanceof GunSmithTableRecipe smithRecipe) {
                smithRecipe.setId(holder.id().location());
                list.add(smithRecipe);
            }
        }
        return list;
    }

    public static Map<ResourceLocation, GunSmithTableRecipe> toMap(List<GunSmithTableRecipe> recipes) {
        Map<ResourceLocation, GunSmithTableRecipe> map = new HashMap<>();
        for (GunSmithTableRecipe recipe : recipes) {
            map.put(recipe.getId(), recipe);
        }
        return map;
    }
}
