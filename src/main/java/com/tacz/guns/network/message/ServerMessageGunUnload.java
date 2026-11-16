package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.client.sound.SoundPlayManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * 仅在服务端确认本次主动卸弹实际减少了枪内弹药后发送给操作者。
 */
public class ServerMessageGunUnload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageGunUnload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "s2c_gun_unload"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerMessageGunUnload> CODEC = CustomPacketPayload.codec(ServerMessageGunUnload::write, ServerMessageGunUnload::decode);

    private final ResourceLocation gunId;
    private final ResourceLocation gunDisplayId;

    public ServerMessageGunUnload(ResourceLocation gunId, ResourceLocation gunDisplayId) {
        this.gunId = gunId;
        this.gunDisplayId = gunDisplayId;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(gunId);
        buf.writeResourceLocation(gunDisplayId);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static ServerMessageGunUnload decode(FriendlyByteBuf buf) {
        return new ServerMessageGunUnload(buf.readResourceLocation(), buf.readResourceLocation());
    }

    @Environment(EnvType.CLIENT)
    public void handle(LocalPlayer player, PacketSender responseSender) {
        playSound();
    }

    @Environment(EnvType.CLIENT)
    private void playSound() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            SoundPlayManager.playUnloadSound(player, gunId, gunDisplayId);
        }
    }
}
