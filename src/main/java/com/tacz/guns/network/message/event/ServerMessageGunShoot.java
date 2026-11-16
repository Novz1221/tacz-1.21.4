package com.tacz.guns.network.message.event;

import cn.sh1rocu.tacz.api.LogicalSide;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.GunShootEvent;
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

public class ServerMessageGunShoot implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageGunShoot> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "s2c_gun_shoot"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerMessageGunShoot> CODEC = CustomPacketPayload.codec(ServerMessageGunShoot::write, ServerMessageGunShoot::new);

    private final int shooterId;
    private final ItemStack gunItemStack;

    public ServerMessageGunShoot(FriendlyByteBuf buf) {
        this(buf.readVarInt(), cn.sh1rocu.tacz.util.ItemNbt.readItem(buf));
    }

    public ServerMessageGunShoot(int shooterId, ItemStack gunItemStack) {
        this.shooterId = shooterId;
        this.gunItemStack = gunItemStack;
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
        doClientEvent(this);
    }

    @Environment(EnvType.CLIENT)
    private static void doClientEvent(ServerMessageGunShoot message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(message.shooterId) instanceof LivingEntity shooter) {
            GunShootEvent gunShootEvent = new GunShootEvent(shooter, message.gunItemStack, LogicalSide.CLIENT);
            GunShootEvent.CALLBACK.invoker().post(gunShootEvent);
        }
    }
}
