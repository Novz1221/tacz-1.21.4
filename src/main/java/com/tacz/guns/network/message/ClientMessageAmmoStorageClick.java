package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.inventory.AmmoStorageMenu;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * A virtual entry interaction, not a vanilla inventory-slot click.
 */
public record ClientMessageAmmoStorageClick(int containerId, int entryId,
                                            AmmoStorageMenu.Action action) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessageAmmoStorageClick> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "c2s_ammo_storage_click"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientMessageAmmoStorageClick> CODEC = CustomPacketPayload.codec(ClientMessageAmmoStorageClick::write, ClientMessageAmmoStorageClick::decode);

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(containerId);
        buf.writeVarInt(entryId);
        buf.writeEnum(action);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static ClientMessageAmmoStorageClick decode(FriendlyByteBuf buf) {
        return new ClientMessageAmmoStorageClick(buf.readVarInt(), buf.readVarInt(), buf.readEnum(AmmoStorageMenu.Action.class));
    }

    public void handle(ServerPlayer player, PacketSender responseSender) {
        if (player != null && player.containerMenu instanceof AmmoStorageMenu menu
                && menu.containerId == containerId) {
            menu.handleInteraction(player, entryId, action);
        }
    }
}
