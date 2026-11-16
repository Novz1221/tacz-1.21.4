package com.tacz.guns.network.message.event;

import cn.sh1rocu.tacz.api.LogicalSide;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.GunFinishReloadEvent;
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

public class ServerMessageGunFinishReload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageGunFinishReload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "s2c_gun_finish_reload"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerMessageGunFinishReload> CODEC = CustomPacketPayload.codec(ServerMessageGunFinishReload::write, ServerMessageGunFinishReload::decode);

    private final int shooterId;
    private final ItemStack gunItemStack;

    public ServerMessageGunFinishReload(int shooterId, ItemStack gunItemStack) {
        this.shooterId = shooterId;
        this.gunItemStack = gunItemStack;
    }

    public static ServerMessageGunFinishReload decode(FriendlyByteBuf buf) {
        int shooterId = buf.readVarInt();
        ItemStack gunItemStack = cn.sh1rocu.tacz.util.ItemNbt.readItem(buf);
        return new ServerMessageGunFinishReload(shooterId, gunItemStack);
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
            GunFinishReloadEvent.EVENT.invoker().post(new GunFinishReloadEvent(shooter, gunItemStack, LogicalSide.CLIENT));
        }
    }
}
