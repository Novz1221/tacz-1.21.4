package com.tacz.guns.resource.serialize;

import com.google.gson.*;
import com.tacz.guns.crafting.GunSmithTableIngredient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GunSmithTableIngredientSerializer implements JsonDeserializer<GunSmithTableIngredient> {
    @Override
    public GunSmithTableIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            if (!jsonObject.has("item")) {
                throw new JsonSyntaxException("Expected " + jsonObject + " must has a item member");
            }
            Ingredient ingredient = parseIngredient(jsonObject.get("item"));
            int count = 1;
            if (jsonObject.has("count")) {
                count = Math.max(GsonHelper.getAsInt(jsonObject, "count"), 1);
            }
            return new GunSmithTableIngredient(ingredient, count);
        } else {
            throw new JsonSyntaxException("Expected " + json + " to be a Pair because it's not an object");
        }
    }

    /** Minimal replacement for Ingredient.fromJson (removed): supports {"item"}, {"tag"} and arrays of those. */
    public static Ingredient parseIngredient(JsonElement element) {
        List<Item> items = new ArrayList<>();
        collect(element, items);
        return Ingredient.of(items.toArray(new Item[0]));
    }

    private static void collect(JsonElement element, List<Item> out) {
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                collect(child, out);
            }
        } else if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("item")) {
                ResourceLocation id = ResourceLocation.parse(GsonHelper.getAsString(object, "item"));
                out.add(BuiltInRegistries.ITEM.getValue(id));
            } else if (object.has("tag")) {
                ResourceLocation id = ResourceLocation.parse(GsonHelper.getAsString(object, "tag"));
                TagKey<Item> tag = TagKey.create(Registries.ITEM, id);
                BuiltInRegistries.ITEM.getValue(tag).ifPresent(set -> set.forEach(holder -> out.add(holder.value())));
            } else {
                throw new JsonSyntaxException("Unsupported ingredient: " + object);
            }
        } else if (element.isJsonPrimitive()) {
            String value = element.getAsString();
            if (value.startsWith("#")) {
                TagKey<Item> tag = TagKey.create(Registries.ITEM, ResourceLocation.parse(value.substring(1)));
                BuiltInRegistries.ITEM.getValue(tag).ifPresent(set -> set.forEach(holder -> out.add(holder.value())));
            } else {
                out.add(BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(value)));
            }
        }
    }
}
