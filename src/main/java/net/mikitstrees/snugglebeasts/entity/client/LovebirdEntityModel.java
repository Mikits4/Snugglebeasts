package net.mikitstrees.snugglebeasts.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mikitstrees.snugglebeasts.entity.custom.LovebirdEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;


public class LovebirdEntityModel extends SinglePartEntityModel<LovebirdEntity> {

    private static final String FEATHER = "feather";
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart head;
    private final ModelPart feather;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public LovebirdEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        this.head = root.getChild("head");
        this.feather = this.head.getChild("feather");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(11, 7).cuboid(-0.5F, -1.5F, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 1).cuboid(-1.0F, -2.5F, -2.0F, 2.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -0.5F));

        ModelPartData feather = head.addChild("feather", ModelPartBuilder.create(), ModelTransform.of(0.0F, -1.5F, -2.0F, -0.2618F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(2, 8).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.5F, -1.0F));

        ModelPartData left_wing = modelPartData.addChild("left_wing", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, 16.9F, -0.8F));

        ModelPartData right_wing = modelPartData.addChild("right_wing", ModelPartBuilder.create(), ModelTransform.pivot(-1.5F, 16.9F, -0.8F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(14, 18).cuboid(-2.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 22.0F, -1.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(14, 18).cuboid(1.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 22.0F, -1.0F));

        ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create().uv(22, 1).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.1F, 1.2F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public void setAngles(LovebirdEntity parrotEntity, float f, float g, float h, float i, float j) {
        this.setAngles(getPose(parrotEntity), parrotEntity.age, f, g, h, i, j);
    }

    public void animateModel(LovebirdEntity parrotEntity, float f, float g, float h) {
        this.animateModel(getPose(parrotEntity));
    }

    public void poseOnShoulder(
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            int light,
            int overlay,
            float limbAngle,
            float limbDistance,
            float headYaw,
            float headPitch,
            int danceAngle
    ) {
        this.animateModel(net.minecraft.client.render.entity.model.ParrotEntityModel.Pose.ON_SHOULDER);
        this.setAngles(net.minecraft.client.render.entity.model.ParrotEntityModel.Pose.ON_SHOULDER, danceAngle, limbAngle, limbDistance, 0.0F, headYaw, headPitch);
        this.root.render(matrices, vertexConsumer, light, overlay);
    }

    private void setAngles(net.minecraft.client.render.entity.model.ParrotEntityModel.Pose pose, int danceAngle, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        this.head.pitch = headPitch * (float) (Math.PI / 180.0);
        this.head.yaw = headYaw * (float) (Math.PI / 180.0);
        this.head.roll = 0.0F;
        this.head.pivotX = 0.0F;
        this.body.pivotX = 0.0F;
        this.tail.pivotX = 0.0F;
        this.rightWing.pivotX = -1.5F;
        this.leftWing.pivotX = 1.5F;
        switch (pose) {
            case STANDING:
                this.leftLeg.pitch = this.leftLeg.pitch + MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
                this.rightLeg.pitch = this.rightLeg.pitch + MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
            case FLYING:
            case ON_SHOULDER:
            default:
                float h = age * 0.3F;
                this.head.pivotY = 15.69F + h;
                this.tail.pitch = 1.015F + MathHelper.cos(limbAngle * 0.6662F) * 0.3F * limbDistance;
                this.tail.pivotY = 21.07F + h;
                this.body.pivotY = 16.5F + h;
                this.leftWing.roll = -0.0873F - age;
                this.leftWing.pivotY = 16.94F + h;
                this.rightWing.roll = 0.0873F + age;
                this.rightWing.pivotY = 16.94F + h;
                this.leftLeg.pivotY = 22.0F + h;
                this.rightLeg.pivotY = 22.0F + h;
            case SITTING:
                break;
            case PARTY:
                float f = MathHelper.cos((float)danceAngle);
                float g = MathHelper.sin((float)danceAngle);
                this.head.pivotX = f;
                this.head.pivotY = 15.69F + g;
                this.head.pitch = 0.0F;
                this.head.yaw = 0.0F;
                this.head.roll = MathHelper.sin((float)danceAngle) * 0.4F;
                this.body.pivotX = f;
                this.body.pivotY = 16.5F + g;
                this.leftWing.roll = -0.0873F - age;
                this.leftWing.pivotX = 1.5F + f;
                this.leftWing.pivotY = 16.94F + g;
                this.rightWing.roll = 0.0873F + age;
                this.rightWing.pivotX = -1.5F + f;
                this.rightWing.pivotY = 16.94F + g;
                this.tail.pivotX = f;
                this.tail.pivotY = 21.07F + g;
        }
    }

    private void animateModel(net.minecraft.client.render.entity.model.ParrotEntityModel.Pose pose) {
        this.feather.pitch = -0.2214F;
        this.body.pitch = 0.4937F;
        this.leftWing.pitch = -0.6981F;
        this.leftWing.yaw = (float) -Math.PI;
        this.rightWing.pitch = -0.6981F;
        this.rightWing.yaw = (float) -Math.PI;
        this.leftLeg.pitch = -0.0299F;
        this.rightLeg.pitch = -0.0299F;
        this.leftLeg.pivotY = 22.0F;
        this.rightLeg.pivotY = 22.0F;
        this.leftLeg.roll = 0.0F;
        this.rightLeg.roll = 0.0F;
        switch (pose) {
            case FLYING:
                this.leftLeg.pitch += (float) (Math.PI * 2.0 / 9.0);
                this.rightLeg.pitch += (float) (Math.PI * 2.0 / 9.0);
            case STANDING:
            case ON_SHOULDER:
            default:
                break;
            case SITTING:
                float f = 1.9F;
                this.head.pivotY = 17.59F;
                this.tail.pitch = 1.5388988F;
                this.tail.pivotY = 22.97F;
                this.body.pivotY = 18.4F;
                this.leftWing.roll = -0.0873F;
                this.leftWing.pivotY = 18.84F;
                this.rightWing.roll = 0.0873F;
                this.rightWing.pivotY = 18.84F;
                this.leftLeg.pivotY++;
                this.rightLeg.pivotY++;
                this.leftLeg.pitch++;
                this.rightLeg.pitch++;
                break;
            case PARTY:
                this.leftLeg.roll = (float) (-Math.PI / 9);
                this.rightLeg.roll = (float) (Math.PI / 9);
        }
    }

    private static net.minecraft.client.render.entity.model.ParrotEntityModel.Pose getPose(LovebirdEntity parrot) {
        if (parrot.isSongPlaying()) {
            return net.minecraft.client.render.entity.model.ParrotEntityModel.Pose.PARTY;
        } else if (parrot.isInSittingPose()) {
            return net.minecraft.client.render.entity.model.ParrotEntityModel.Pose.SITTING;
        } else {
            return parrot.isInAir() ? net.minecraft.client.render.entity.model.ParrotEntityModel.Pose.FLYING : net.minecraft.client.render.entity.model.ParrotEntityModel.Pose.STANDING;
        }
    }

    @Environment(EnvType.CLIENT)
    public static enum Pose {
        FLYING,
        STANDING,
        SITTING,
        PARTY,
        ON_SHOULDER;
    }
}