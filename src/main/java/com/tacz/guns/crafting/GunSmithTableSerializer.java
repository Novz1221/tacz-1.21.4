package com.tacz.guns.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.tacz.guns.crafting.result.GunSmithTableResult;
import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.pojo.data.recipe.TableRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 工作台配方序列化器
 */
public class GunSmithTableSerializer implements RecipeSerializer<GunSmithTableRecipe> {
    /** Reads the whole recipe object with the existing Gson pipeline. */
    private static final MapCodec<GunSmithTableRecipe> CODEC = new MapCodec<>() {
        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of(ops.createString("materials"), ops.createString("result"));
        }

        @Override
        public <T> DataResult<GunSmithTableRecipe> decode(DynamicOps<T> ops, MapLike<T> input) {
            try {
                JsonObject object = new JsonObject();
                input.entries().forEach(pair -> {
                    String key = ops.getStringValue(pair.getFirst()).getOrThrow();
                    if ("type".equals(key)) {
                        return;
                    }
                    JsonElement value = new Dynamic<>(ops, pair.getSecond()).convert(JsonOps.INSTANCE).getValue();
                    object.add(key, value);
                });
                TableRecipe tableRecipe = CommonAssetsManager.GSON.fromJson(object, TableRecipe.class);
                if (tableRecipe == null) {
                    return DataResult.error(() -> "Empty gun smith table recipe");
                }
                return DataResult.success(new GunSmithTableRecipe(tableRecipe));
            } catch (Exception e) {
                return DataResult.error(() -> "Invalid gun smith table recipe: " + e.getMessage());
            }
        }

        @Override
        public <T> RecordBuilder<T> encode(GunSmithTableRecipe input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return prefix;
        }
    };

    private static final StreamCodec<RegistryFriendlyByteBuf, GunSmithTableRecipe> STREAM_CODEC =
            StreamCodec.of((buf, recipe) -> write(buf, recipe), buf -> read(buf));

    @Override
    public MapCodec<GunSmithTableRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GunSmithTableRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    public static GunSmithTableRecipe read(FriendlyByteBuf plain) {
        RegistryFriendlyByteBuf buffer = (RegistryFriendlyByteBuf) plain;
        int size = buffer.readInt();
        List<GunSmithTableIngredient> ingredients = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ingredients.add(new GunSmithTableIngredient(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readInt()));
        }
        ItemStack resultItem = ItemStack.STREAM_CODEC.decode(buffer);
        resultItem.setCount(buffer.readVarInt());
        ResourceLocation group = buffer.readResourceLocation();
        GunSmithTableResult result = new GunSmithTableResult(resultItem, group);
        return new GunSmithTableRecipe(result, ingredients);
    }

    public static void write(FriendlyByteBuf plain, GunSmithTableRecipe recipe) {
        RegistryFriendlyByteBuf buffer = (RegistryFriendlyByteBuf) plain;
        buffer.writeInt(recipe.getInputs().size());
        for (GunSmithTableIngredient ingredient : recipe.getInputs()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient.getIngredient());
            buffer.writeInt(ingredient.getCount());
        }
        ItemStack result = recipe.getResult().getResult();
        ItemStack.STREAM_CODEC.encode(buffer, result.copyWithCount(1));
        buffer.writeVarInt(result.getCount());
        buffer.writeResourceLocation(recipe.getResult().getGroup());
    }
}
