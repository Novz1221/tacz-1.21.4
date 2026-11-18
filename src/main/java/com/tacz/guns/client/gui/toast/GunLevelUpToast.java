package com.tacz.guns.client.gui.toast;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
public class GunLevelUpToast implements Toast {
    private final Component title;
    private final Component subTitle;
    private final ItemStack icon;
    private Visibility wantedVisibility = Visibility.SHOW;

    public GunLevelUpToast(ItemStack icon, Component titleComponent, @Nullable Component subtitle) {
        this.icon = icon;
        this.title = titleComponent;
        this.subTitle = subtitle;
    }

    @NotNull
    @Override
    public Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(ToastManager toastManager, long visibleTime) {
        this.wantedVisibility = visibleTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics gui, Font font, long visibleTime) {
        // todo 这个类没有实际使用，先不管了
    }
}
