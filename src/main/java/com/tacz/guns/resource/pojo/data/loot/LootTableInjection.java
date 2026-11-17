package com.tacz.guns.resource.pojo.data.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO(1.21.4): loot tables are now codec based and live in ReloadableServerRegistries, so the old
 * Gson based LootTable parsing was removed. Injections are parsed and kept but currently produce no items.
 */
public record LootTableInjection(List<ResourceLocation> lootTables, JsonObject definition, ResourceLocation id) {
    public static LootTableInjection fromJson(ResourceLocation fileId, JsonElement element) {
        JsonObject object = GsonHelper.convertToJsonObject(element, "loot injection");
        List<ResourceLocation> lootTables = readLootTables(fileId, object);
        if (!object.has("pools")) {
            throw new JsonParseException("Loot injection " + fileId + " must define pools");
        }
        return new LootTableInjection(lootTables, object, fileId);
    }

    private static List<ResourceLocation> readLootTables(ResourceLocation fileId, JsonObject object) {
        List<ResourceLocation> lootTables = new ArrayList<>();
        if (object.has("loot_tables")) {
            for (JsonElement table : GsonHelper.getAsJsonArray(object, "loot_tables")) {
                lootTables.add(ResourceLocation.parse(GsonHelper.convertToString(table, "loot table")));
            }
        } else if (object.has("loot_table")) {
            lootTables.add(ResourceLocation.parse(GsonHelper.getAsString(object, "loot_table")));
        } else {
            throw new JsonParseException("Loot injection " + fileId + " must define loot_table or loot_tables");
        }
        return List.copyOf(lootTables);
    }

    public List<ItemStack> createStacks(LootContext context) {
        return new ArrayList<>();
    }
}
