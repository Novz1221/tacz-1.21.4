package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.gun.GunItemManager;
import com.tacz.guns.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ModItems {
    public static void init() {
        GunItemManager.registerGunItem(ModernKineticGunItem.TYPE_NAME, MODERN_KINETIC_GUN);
    }

    public static ModernKineticGunItem MODERN_KINETIC_GUN = register("modern_kinetic_gun", () -> new ModernKineticGunItem());

//    public static ThrowableItem M67 = register("m67", () -> new ThrowableItem());

    public static Item AMMO = register("ammo", () -> new AmmoItem());
    public static AttachmentItem ATTACHMENT = register("attachment", () -> new AttachmentItem());

    public static GunSmithTableItem GUN_SMITH_TABLE = register("gun_smith_table", () -> new DefaultTableItem(ModBlocks.GUN_SMITH_TABLE));
    public static GunSmithTableItem WORKBENCH_111 = register("workbench_a", () -> new GunSmithTableItem(ModBlocks.WORKBENCH_111));
    public static GunSmithTableItem WORKBENCH_211 = register("workbench_b", () -> new GunSmithTableItem(ModBlocks.WORKBENCH_211));
    public static GunSmithTableItem WORKBENCH_121 = register("workbench_c", () -> new GunSmithTableItem(ModBlocks.WORKBENCH_121));


    public static Item TARGET = register("target", () -> new BlockItem(ModBlocks.TARGET, cn.sh1rocu.tacz.util.IdProps.item()));
    public static Item STATUE = register("statue", () -> new BlockItem(ModBlocks.STATUE, cn.sh1rocu.tacz.util.IdProps.item()));
    public static Item GUN_PEGBOARD = register("gun_pegboard", () -> new GunPegboardItem(ModBlocks.GUN_PEGBOARD, cn.sh1rocu.tacz.util.IdProps.item()));
    public static Item GLOW_GUN_PEGBOARD = register("glow_gun_pegboard", () -> new GunPegboardItem(ModBlocks.GLOW_GUN_PEGBOARD, cn.sh1rocu.tacz.util.IdProps.item()));
    public static Item AMMO_STORAGE = register("ammo_storage", () -> new AmmoStorageBlockItem(ModBlocks.AMMO_STORAGE));
    public static Item AMMO_STORAGE_GOLD = register("ammo_storage_gold", () -> new AmmoStorageBlockItem(ModBlocks.AMMO_STORAGE_GOLD));
    public static Item AMMO_STORAGE_DIAMOND = register("ammo_storage_diamond", () -> new AmmoStorageBlockItem(ModBlocks.AMMO_STORAGE_DIAMOND));
    public static Item GUN_DISPLAY_CASE = register("gun_display_case", () -> new BlockItem(ModBlocks.GUN_DISPLAY_CASE, cn.sh1rocu.tacz.util.IdProps.item()) {
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
            tooltip.add(Component.translatable("tooltip.tacz.gun_display_case").withStyle(ChatFormatting.GRAY));
        }
    });
    public static Item AMMO_BOX = register("ammo_box", () -> new AmmoBoxItem());
    public static Item TARGET_MINECART = register("target_minecart", () -> new TargetMinecartItem());

    private static <T extends Item> T register(String name, java.util.function.Supplier<T> factory) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.ITEM, id, cn.sh1rocu.tacz.util.IdProps.withId(id, factory));
    }
}
