package com.tacz.guns.network.message.event;

import cn.sh1rocu.tacz.api.LogicalSide;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.GunFirePostEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ServerMessageGunFirePost implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageGunFirePost> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "s2c_gunfire_post"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerMessageGunFirePost> CODEC = CustomPacketPayload.codec(ServerMessageGunFirePost::write, ServerMessageGunFirePost::decode);

    private final int shooterId;
    private final ItemStack gunItemStack;

    public ServerMessageGunFirePost(int shooterId, ItemStack gunItemStack) {
        this.shooterId = shooterId;
        this.gunItemStack = gunItemStack;
    }

    public static void encode(ServerMessageGunFirePost message, FriendlyByteBuf buf) {
    }

    public static ServerMessageGunFirePost decode(FriendlyByteBuf buf) {
        int shooterId = buf.readVarInt();
        ItemStack gunItemStack = cn.sh1rocu.tacz.util.ItemNbt.readItem(buf);
        return new ServerMessageGunFirePost(shooterId, gunItemStack);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(shooterId);
        cn.sh1rocu.tacz.util.ItemNbt.writeItem(buf, gunItemStack);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Environment(EnvType.CLIENT)
    public void handle(LocalPlayer player, PacketSender responseSender) {
        doClientEvent();
    }

    @Environment(EnvType.CLIENT)
    private void doClientEvent() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(shooterId) instanceof LivingEntity shooter) {
            GunFirePostEvent.EVENT.invoker().post(new GunFirePostEvent(shooter, gunItemStack, LogicalSide.CLIENT));
        }
    }
}
