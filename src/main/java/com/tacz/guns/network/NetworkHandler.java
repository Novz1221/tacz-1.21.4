package com.tacz.guns.network;

import cn.sh1rocu.tacz.api.extension.IEntityAdditionalSpawnData;
import com.tacz.guns.network.message.*;
import com.tacz.guns.network.message.event.*;
import com.tacz.guns.network.message.handshake.AcknowledgeC2SPacket;
import com.tacz.guns.network.message.handshake.SyncedEntityDataMappingS2CPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.mixin.networking.client.accessor.ClientLoginNetworkHandlerAccessor;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class NetworkHandler {
    public static void registerC2SPackets() {
        // Payload types must be registered on both sides before use (1.20.5+ networking)
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerShoot.TYPE, ClientMessagePlayerShoot.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerReloadGun.TYPE, ClientMessagePlayerReloadGun.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerCancelReload.TYPE, ClientMessagePlayerCancelReload.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerFireSelect.TYPE, ClientMessagePlayerFireSelect.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerAim.TYPE, ClientMessagePlayerAim.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerCrawl.TYPE, ClientMessagePlayerCrawl.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerDrawGun.TYPE, ClientMessagePlayerDrawGun.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageCraft.TYPE, ClientMessageCraft.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerZoom.TYPE, ClientMessagePlayerZoom.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageRefitGun.TYPE, ClientMessageRefitGun.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageInstallVirtualAttachment.TYPE, ClientMessageInstallVirtualAttachment.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageUnloadAttachment.TYPE, ClientMessageUnloadAttachment.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerBoltGun.TYPE, ClientMessagePlayerBoltGun.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerMelee.TYPE, ClientMessagePlayerMelee.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageSyncBaseTimestamp.TYPE, ClientMessageSyncBaseTimestamp.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageLaserColor.TYPE, ClientMessageLaserColor.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageAmmoStorageInteract.TYPE, ClientMessageAmmoStorageInteract.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageAmmoStorageClick.TYPE, ClientMessageAmmoStorageClick.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageSetAttachmentVariant.TYPE, ClientMessageSetAttachmentVariant.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessageSetSlotAdapter.TYPE, ClientMessageSetSlotAdapter.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientMessagePlayerUnloadGun.TYPE, ClientMessagePlayerUnloadGun.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageAmmoStorageContents.TYPE, ServerMessageAmmoStorageContents.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageSound.TYPE, ServerMessageSound.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageBulletHitBlock.TYPE, ServerMessageBulletHitBlock.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageCraft.TYPE, ServerMessageCraft.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageRefreshRefitScreen.TYPE, ServerMessageRefreshRefitScreen.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageSwapItem.TYPE, ServerMessageSwapItem.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageLevelUp.TYPE, ServerMessageLevelUp.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunHurt.TYPE, ServerMessageGunHurt.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunKill.TYPE, ServerMessageGunKill.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageUpdateEntityData.TYPE, ServerMessageUpdateEntityData.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageSyncGunPack.TYPE, ServerMessageSyncGunPack.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageSyncSmithRecipes.TYPE, ServerMessageSyncSmithRecipes.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunDraw.TYPE, ServerMessageGunDraw.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunFinishReload.TYPE, ServerMessageGunFinishReload.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunFire.TYPE, ServerMessageGunFire.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunFirePost.TYPE, ServerMessageGunFirePost.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunFireSelect.TYPE, ServerMessageGunFireSelect.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunMelee.TYPE, ServerMessageGunMelee.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunReload.TYPE, ServerMessageGunReload.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunShoot.TYPE, ServerMessageGunShoot.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageSyncBaseTimestamp.TYPE, ServerMessageSyncBaseTimestamp.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerMessageGunUnload.TYPE, ServerMessageGunUnload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerShoot.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerReloadGun.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerCancelReload.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerFireSelect.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerAim.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerCrawl.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerDrawGun.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageCraft.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerZoom.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageRefitGun.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageInstallVirtualAttachment.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageUnloadAttachment.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerBoltGun.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerMelee.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageSyncBaseTimestamp.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageLaserColor.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageAmmoStorageInteract.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageAmmoStorageClick.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageSetAttachmentVariant.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessageSetSlotAdapter.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ServerPlayNetworking.registerGlobalReceiver(ClientMessagePlayerUnloadGun.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));

        HandshakeNetworking.register(AcknowledgeC2SPacket.ID, AcknowledgeC2SPacket.class);
        HandshakeNetworking.register(SyncedEntityDataMappingS2CPacket.TYPE, SyncedEntityDataMappingS2CPacket.class);
    }

    @Environment(EnvType.CLIENT)
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(IEntityAdditionalSpawnData.EXTRA_DATA_PACKET, (client, handler, buf, responseSender) -> {
            int entityId = buf.readVarInt();
            buf.retain();
            client.execute(() -> {
                Entity entity = Objects.requireNonNull(client.level).getEntity(entityId);
                if (entity instanceof IEntityAdditionalSpawnData extra) {
                    extra.readSpawnData(buf);
                }
                buf.release();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerMessageAmmoStorageContents.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageSound.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageBulletHitBlock.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageCraft.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageRefreshRefitScreen.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageSwapItem.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageLevelUp.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunHurt.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunKill.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageUpdateEntityData.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageSyncSmithRecipes.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageSyncGunPack.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunDraw.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunFinishReload.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunFire.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunFirePost.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunFireSelect.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunMelee.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunReload.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunShoot.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageSyncBaseTimestamp.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
        ClientPlayNetworking.registerGlobalReceiver(ServerMessageGunUnload.TYPE, (payload, ctx) -> payload.handle(ctx.player(), ctx.responseSender()));
    }

    @SuppressWarnings("UnstableApiUsage")
    @Environment(EnvType.CLIENT)
    static <T extends IHandshakeMessage> void registerHandshake(PacketType<T> type) {
        ClientLoginNetworking.registerGlobalReceiver(type.getId(), (client, handler, buf, listenerAdder) -> {
            T packet = type.read(buf);
            Connection connection = ((ClientLoginNetworkHandlerAccessor) handler).getConnection();
            IHandshakeMessage.IResponsePacket responsePacket = packet.handle(connection, listenerAdder);
            FriendlyByteBuf response = PacketByteBufs.create();
            if (responsePacket != null) {
                response.writeResourceLocation(responsePacket.getId());
                responsePacket.write(response);
            }
            return CompletableFuture.completedFuture(response);
        });
    }

    public static void sendToClientPlayer(CustomPacketPayload message, ServerPlayer player) {
        ServerPlayNetworking.send(player, message);
    }

    /**
     * 发送给所有监听此实体的玩家
     */
    public static void sendToTrackingEntityAndSelf(Entity centerEntity, CustomPacketPayload message) {
        if (centerEntity instanceof ServerPlayer player) {
            sendToClientPlayer(message, player);
        }
        sendToTrackingEntity(message, centerEntity);
    }

    public static void sendToAllPlayers(CustomPacketPayload message, MinecraftServer server) {
        for (ServerPlayer player : PlayerLookup.all(server)) {
            ServerPlayNetworking.send(player, message);
        }
    }

    public static void sendToTrackingEntity(CustomPacketPayload message, final Entity centerEntity) {
        for (ServerPlayer player : PlayerLookup.tracking(centerEntity)) {
            ServerPlayNetworking.send(player, message);
        }
    }

    public static void sendToDimension(CustomPacketPayload message, final Entity centerEntity) {
        if (centerEntity.level() instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : PlayerLookup.world(serverLevel)) {
                ServerPlayNetworking.send(player, message);
            }
        }
    }
}
