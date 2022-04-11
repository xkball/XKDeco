package org.teacon.xkdeco.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class SmallCushionModel extends EntityModel<Entity> {
    private final ModelPart body;

    public SmallCushionModel(ModelPart root) {
        this.body = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation def) {
        var mesh = new MeshDefinition();
        var rotation = PartPose.rotation(0, 24, 0);
        var box = CubeListBuilder.create().texOffs(0, 0).addBox(-3, -1, -3, 6, 1, 6, def);
        mesh.getRoot().addOrReplaceChild("body", box, rotation);
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity,
                          float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.setRotation(headPitch * Mth.DEG_TO_RAD, netHeadYaw * Mth.DEG_TO_RAD, 0);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
