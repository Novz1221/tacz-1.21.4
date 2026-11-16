package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ClientMessagePlayerCrawl implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessagePlayerCrawl> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "c2s_player_crawl"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientMessagePlayerCrawl> CODEC = CustomPacketPayload.codec(ClientMessagePlayerCrawl::write, ClientMessagePlayerCrawl::new);

    private final boolean isCrawl;

    public ClientMessagePlayerCrawl(boolean isCrawl) {
        this.isCrawl = isCrawl;
    }

    public ClientMessagePlayerCrawl(FriendlyByteBuf buf) {
        this.isCrawl = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(isCrawl);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerPlayer entity, PacketSender responseSender) {
        if (entity == null) {
            return;
        }
        // 进入条件由服务端趴下逻辑统一验证；禁用功能后仍允许退出。
        IGunOperator.fromLivingEntity(entity).crawl(isCrawl);
    }
}
