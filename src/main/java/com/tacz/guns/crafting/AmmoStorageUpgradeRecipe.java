package com.tacz.guns.crafting;

import com.tacz.guns.init.ModItems;
import com.tacz.guns.init.ModRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AmmoStorageUpgradeRecipe extends CustomRecipe {
    private final boolean diamond;
    private final Item from;
    private final Item material;
    private final Item to;

    public AmmoStorageUpgradeRecipe(CraftingBookCategory category, boolean diamond) {
        super(category);
        this.diamond = diamond;
        if (diamond) {
            from = ModItems.AMMO_STORAGE_GOLD;
            material = Items.DIAMOND;
            to = ModItems.AMMO_STORAGE_DIAMOND;
        } else {
            from = ModItems.AMMO_STORAGE;
            material = Items.GOLD_INGOT;
            to = ModItems.AMMO_STORAGE_GOLD;
        }
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (slot == 4) {
                if (!stack.is(from)) {
                    return false;
                }
            } else if (!stack.is(material)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = new ItemStack(to);
        CustomData blockEntityData = input.getItem(4).get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData != null) {
            result.set(DataComponents.BLOCK_ENTITY_DATA, blockEntityData);
        }
        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return diamond ? ModRecipe.AMMO_STORAGE_UPGRADE_DIAMOND_RECIPE_SERIALIZER : ModRecipe.AMMO_STORAGE_UPGRADE_RECIPE_SERIALIZER;
    }
}
