package cn.sh1rocu.tacz.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * Since 1.21.2 items and blocks must know their registry id when their Properties are created.
 * Registration helpers run the constructor inside withId(...), and item()/block(...) pick the id up.
 */
public final class IdProps {
    private static final ThreadLocal<ResourceLocation> CURRENT = new ThreadLocal<>();

    private IdProps() {}

    public static <T> T withId(ResourceLocation id, Supplier<T> supplier) {
        ResourceLocation previous = CURRENT.get();
        CURRENT.set(id);
        try {
            return supplier.get();
        } finally {
            if (previous == null) CURRENT.remove(); else CURRENT.set(previous);
        }
    }

    public static Item.Properties item() {
        Item.Properties properties = new Item.Properties();
        ResourceLocation id = CURRENT.get();
        return id == null ? properties : properties.setId(ResourceKey.create(Registries.ITEM, id));
    }

    public static BlockBehaviour.Properties block(BlockBehaviour.Properties properties) {
        ResourceLocation id = CURRENT.get();
        return id == null ? properties : properties.setId(ResourceKey.create(Registries.BLOCK, id));
    }
}
