package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.crafting.GunSmithRecipes;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.crafting.GunSmithTableSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ServerMessageSyncSmithRecipes implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageSyncSmithRecipes> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "s2c_sync_smith_recipes"));
    public static final StreamCodec<FriendlyByteBuf, ServerMessageSyncSmithRecipes> CODEC = CustomPacketPayload.codec(ServerMessageSyncSmithRecipes::write, ServerMessageSyncSmithRecipes::new);

    private final List<GunSmithTableRecipe> recipes;

    public ServerMessageSyncSmithRecipes(List<GunSmithTableRecipe> recipes) {
        this.recipes = recipes;
    }

    public ServerMessageSyncSmithRecipes(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.recipes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            GunSmithTableRecipe recipe = GunSmithTableSerializer.read(buf);
            recipe.setId(id);
            recipes.add(recipe);
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(recipes.size());
        for (GunSmithTableRecipe recipe : recipes) {
            buf.writeResourceLocation(recipe.getId());
            GunSmithTableSerializer.write(buf, recipe);
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Environment(EnvType.CLIENT)
    public void handle(LocalPlayer player, PacketSender responseSender) {
        recipes.forEach(GunSmithTableRecipe::init);
        GunSmithRecipes.setClient(GunSmithRecipes.toMap(recipes));
    }
}
