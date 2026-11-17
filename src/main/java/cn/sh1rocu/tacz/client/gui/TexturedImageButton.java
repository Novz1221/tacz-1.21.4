package cn.sh1rocu.tacz.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/** Old-style texture-region button (the pre-1.20.2 ImageButton constructor). */
public class TexturedImageButton extends ImageButton {
    private static final ResourceLocation DUMMY = ResourceLocation.withDefaultNamespace("widget/button");
    private final ResourceLocation texture;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;

    public TexturedImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex,
                               ResourceLocation texture, OnPress onPress) {
        this(x, y, width, height, xTexStart, yTexStart, yDiffTex, texture, 256, 256, onPress);
    }

    public TexturedImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex,
                               ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress) {
        super(x, y, width, height, new WidgetSprites(DUMMY, DUMMY), onPress);
        this.texture = texture;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int v = this.yTexStart + (this.isHoveredOrFocused() ? this.yDiffTex : 0);
        guiGraphics.blit(RenderType::guiTextured, this.texture, this.getX(), this.getY(),
                (float) this.xTexStart, (float) v, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
