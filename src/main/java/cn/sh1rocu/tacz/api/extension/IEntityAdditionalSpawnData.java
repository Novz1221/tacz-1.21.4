package cn.sh1rocu.tacz.api.extension;

import com.tacz.guns.GunMod;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;

import java.util.List;

// Porting_Lib
public interface IEntityAdditionalSpawnData {
    void readSpawnData(FriendlyByteBuf buf);

    void writeSpawnData(FriendlyByteBuf buf);

    /** Extra spawn data, sent together with the vanilla add-entity packet in a bundle. */
    record Payload(int entityId, byte[] data) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Payload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "extra_entity_spawn_data"));
        public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = StreamCodec.of(
                (buf, payload) -> {
                    buf.writeVarInt(payload.entityId);
                    buf.writeByteArray(payload.data);
                },
                buf -> new Payload(buf.readVarInt(), buf.readByteArray()));

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    static Packet<ClientGamePacketListener> getEntitySpawningPacket(Entity entity, ServerEntity serverEntity) {
        return getEntitySpawningPacket(entity, new ClientboundAddEntityPacket(entity, serverEntity));
    }

    static Packet<ClientGamePacketListener> getEntitySpawningPacket(Entity entity, Packet<ClientGamePacketListener> base) {
        if (entity instanceof IEntityAdditionalSpawnData extra) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            extra.writeSpawnData(buf);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            buf.release();
            List<Packet<? super ClientGamePacketListener>> packets = List.of(base, new ClientboundCustomPayloadPacket(new Payload(entity.getId(), data)));
            return new ClientboundBundlePacket(packets);
        }
        return base;
    }
}
