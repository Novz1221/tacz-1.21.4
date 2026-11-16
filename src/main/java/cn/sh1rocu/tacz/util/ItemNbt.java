package cn.sh1rocu.tacz.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;

/**
 * Compatibility shim: 1.20.1 code stored item NBT directly on the stack (getOrCreateTag/getTag).
 * Since 1.20.5 this lives in the CUSTOM_DATA data component. This keeps the old call style working.
 */
@SuppressWarnings("deprecation")
public final class ItemNbt {
    private ItemNbt() {}

    /** Returns a live, mutable tag that is stored on the stack (changes persist). */
    public static CompoundTag getOrCreateTag(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag copy = data == null ? new CompoundTag() : data.copyTag();
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(copy));
        return stack.get(DataComponents.CUSTOM_DATA).getUnsafe();
    }

    /** Read-only view of the stack's custom data, or null. */
    @Nullable
    public static CompoundTag getTag(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? null : data.getUnsafe();
    }

    public static boolean hasTag(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
    }

    public static void setTag(ItemStack stack, @Nullable CompoundTag tag) {
        if (tag == null) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public static ItemStack readItem(FriendlyByteBuf buf) {
        if (buf instanceof RegistryFriendlyByteBuf r) {
            return ItemStack.OPTIONAL_STREAM_CODEC.decode(r);
        }
        throw new IllegalStateException("readItem needs a RegistryFriendlyByteBuf");
    }

    public static void writeItem(FriendlyByteBuf buf, ItemStack stack) {
        if (buf instanceof RegistryFriendlyByteBuf r) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(r, stack);
            return;
        }
        throw new IllegalStateException("writeItem needs a RegistryFriendlyByteBuf");
    }
}
