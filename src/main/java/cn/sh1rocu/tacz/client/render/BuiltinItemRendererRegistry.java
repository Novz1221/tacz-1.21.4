package cn.sh1rocu.tacz.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/** Replacement for Fabric's removed BuiltinItemRendererRegistry. */
public final class BuiltinItemRendererRegistry {
    public static final BuiltinItemRendererRegistry INSTANCE = new BuiltinItemRendererRegistry();
    private final Map<Item, DynamicItemRenderer> renderers = new HashMap<>();

    private BuiltinItemRendererRegistry() {}

    public void register(ItemLike item, DynamicItemRenderer renderer) {
        renderers.put(item.asItem(), renderer);
    }

    @Nullable
    public DynamicItemRenderer get(ItemLike item) {
        return renderers.get(item.asItem());
    }

    @FunctionalInterface
    public interface DynamicItemRenderer {
        void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay);
    }
}
