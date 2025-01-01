package me.rufia.fightorflight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import me.rufia.fightorflight.entity.projectile.PokemonArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class PokemonArrowRenderer extends EntityRenderer<PokemonArrow> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(CobblemonFightOrFlight.MODID, "textures/entity/arrow_bullet.png");

    public PokemonArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PokemonArrow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Color color = Color.white;
        if (entity.getElementalType() != null) {
            color = PokemonAttackEffect.getColorFromType(entity.getElementalType());
        }
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        boolean i = false;
        float f = 0.0F;
        float g = 0.5F;
        float h = 0.0F;
        float j = 0.15625F;
        float k = 0.0F;
        float l = 0.15625F;
        float m = 0.15625F;
        float n = 0.3125F;
        float o = 0.05625F;
        float p = 0f - partialTicks;
        if (p > 0.0F) {
            float q = -Mth.sin(p * 3.0F) * p;
            poseStack.mulPose(Axis.ZP.rotationDegrees(q));
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        PoseStack.Pose pose = poseStack.last();

        this.vertex(pose,  vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight, color);
        this.vertex(pose,  vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight, color);

        for (int r = 0; r < 4; ++r) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex(pose, vertexConsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight, color);
            this.vertex(pose, vertexConsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight, color);
            this.vertex(pose, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight, color);
            this.vertex(pose, vertexConsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight, color);
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int packedLight, Color col) {
        consumer.addVertex(pose, (float) x, (float) y, (float) z).setColor(col.getRed(), col.getGreen(), col.getBlue(), 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float) normalX, (float) normalY, (float) normalZ);
    }

    public ResourceLocation getTextureLocation(PokemonArrow entity) {
        return TEXTURE_LOCATION;
    }
}
