package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.inventory.GunSmithTableMenu;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ClientMessageCraft implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessageCraft> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "c2s_craft"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientMessageCraft> CODEC = CustomPacketPayload.codec(ClientMessageCraft::write, ClientMessageCraft::new);

    private final ResourceLocation recipeId;
    private final int menuId;

    public ClientMessageCraft(FriendlyByteBuf buf) {
        this(buf.readResourceLocation(), buf.readVarInt());
    }

    public ClientMessageCraft(ResourceLocation recipeId, int menuId) {
        this.recipeId = recipeId;
        this.menuId = menuId;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(recipeId);
        buf.writeVarInt(menuId);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerPlayer player, PacketSender responseSender) {
        if (player.containerMenu.containerId == menuId && player.containerMenu instanceof GunSmithTableMenu menu) {
            menu.doCraft(recipeId, player);
        }
    }
}
