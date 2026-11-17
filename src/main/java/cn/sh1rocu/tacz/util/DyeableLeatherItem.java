package cn.sh1rocu.tacz.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.List;

/** Replacement for the removed vanilla DyeableLeatherItem, backed by the DYED_COLOR component. */
public interface DyeableLeatherItem {
    default boolean hasCustomColor(ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR);
    }

    default int getColor(ItemStack stack) {
        DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
        return color == null ? 0xA06540 : color.rgb();
    }

    default void clearColor(ItemStack stack) {
        stack.remove(DataComponents.DYED_COLOR);
    }

    default void setColor(ItemStack stack, int color) {
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color, false));
    }

    static ItemStack dyeArmor(ItemStack stack, List<DyeItem> dyes) {
        return DyedItemColor.applyDyes(stack, dyes);
    }
}
