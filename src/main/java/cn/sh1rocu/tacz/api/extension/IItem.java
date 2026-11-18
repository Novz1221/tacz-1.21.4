package cn.sh1rocu.tacz.api.extension;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import cn.sh1rocu.tacz.client.render.BuiltinItemRendererRegistry;
import cn.sh1rocu.tacz.client.render.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItem {
    default int tacz$getMaxStackSize(ItemStack stack) {
        return ((Item) this).getDefaultMaxStackSize();
    }

    default boolean tacz$onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    BuiltinItemRendererRegistry.DynamicItemRenderer getCustomRenderer();
}
