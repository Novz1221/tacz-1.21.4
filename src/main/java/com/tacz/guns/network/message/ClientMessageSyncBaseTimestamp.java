package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import com.tacz.guns.util.time.GunTime;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ClientMessageSyncBaseTimestamp implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessageSyncBaseTimestamp> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "c2s_sync_base_timestamp"));
    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientMessageSyncBaseTimestamp> CODEC = CustomPacketPayload.codec(ClientMessageSyncBaseTimestamp::write, ClientMessageSyncBaseTimestamp::new);

    private static final Marker MARKER = MarkerFactory.getMarker("SYNC_BASE_TIMESTAMP");

    public ClientMessageSyncBaseTimestamp(FriendlyByteBuf buf) {
        this();
    }

    public ClientMessageSyncBaseTimestamp() {

    }

    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerPlayer entity, PacketSender responseSender) {
        long timestamp = GunTime.nowMillis();
        if (entity == null) {
            return;
        }
        ShooterDataHolder dataHolder = IGunOperator.fromLivingEntity(entity).getDataHolder();
        dataHolder.baseTimestamp = timestamp;
        GunMod.LOGGER.debug(MARKER, "Update server base timestamp: {}", dataHolder.baseTimestamp);
    }
}
