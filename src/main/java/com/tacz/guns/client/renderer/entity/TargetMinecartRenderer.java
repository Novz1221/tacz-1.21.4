package com.tacz.guns.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tacz.guns.client.model.bedrock.BedrockModel;
import com.tacz.guns.client.model.bedrock.BedrockPart;
import com.tacz.guns.client.resource.InternalAssetLoader;
import com.tacz.guns.entity.TargetMinecart;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class TargetMinecartRenderer extends net.minecraft.client.renderer.entity.AbstractMinecartRenderer<TargetMinecart, TargetMinecartRenderer.TargetState> {
    public static class TargetState extends net.minecraft.client.renderer.entity.state.MinecartRenderState {
        public GameProfile gameProfile;
    }

    @Override
    public TargetState createRenderState() {
        return new TargetState();
    }

    @Override
    public void extractRenderState(TargetMinecart minecart, TargetState state, float partialTicks) {
        super.extractRenderState(minecart, state, partialTicks);
        state.gameProfile = minecart.getGameProfile();
    }

    private static final String HEAD_NAME = "head";
    private static final String HEAD_2_NAME = "head2";

    public TargetMinecartRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, ModelLayers.TNT_MINECART);
    }

    public static Optional<BedrockModel> getModel() {
        return InternalAssetLoader.getBedrockModel(InternalAssetLoader.TARGET_MINECART_MODEL_LOCATION);
    }

    @Override
    protected void renderMinecartContents(TargetState targetMinecart, BlockState pState, PoseStack stack, MultiBufferSource buffer, int pPackedLight) {
        getModel().ifPresent(model -> {
            BedrockPart headModel = model.getNode(HEAD_NAME);
            BedrockPart head2Model = model.getNode(HEAD_2_NAME);
            headModel.visible = false;
            head2Model.visible = false;

            stack.pushPose();
            stack.translate(0.5, 1.875, 0.5);
            stack.scale(1.5f, 1.5f, 1.5f);
            stack.mulPose(Axis.ZN.rotationDegrees(180));
            stack.mulPose(Axis.YN.rotationDegrees(90));
            RenderType renderType = RenderType.entityTranslucent(InternalAssetLoader.TARGET_MINECART_TEXTURE_LOCATION);
            model.render(stack, ItemDisplayContext.NONE, renderType, pPackedLight, OverlayTexture.NO_OVERLAY);
            if (targetMinecart.gameProfile != null) {
                stack.translate(0, 1, -4.5 / 16d);
                Minecraft minecraft = Minecraft.getInstance();
                GameProfile gameProfile = targetMinecart.gameProfile;
                ResourceLocation skin = minecraft.getSkinManager().getInsecureSkin(gameProfile).texture();
                headModel.visible = true;
                RenderType skullRenderType = RenderType.entityTranslucent(skin);
                headModel.render(stack, ItemDisplayContext.NONE, buffer.getBuffer(skullRenderType), pPackedLight, OverlayTexture.NO_OVERLAY);

                head2Model.visible = true;
                stack.translate(0, 0, 0.01);
                head2Model.render(stack, ItemDisplayContext.NONE, buffer.getBuffer(skullRenderType), pPackedLight, OverlayTexture.NO_OVERLAY);
            }
            stack.popPose();
        });
    }
}