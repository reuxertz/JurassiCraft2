package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.animation.entity.LegArticulator;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;
import org.jurassicraft.server.entity.LegSolverQuadruped;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.JsonAnimationModule;

public class ArticulateQuadruped extends JsonAnimationModule<ArticulateQuadruped.Info> {

    public ArticulateQuadruped(JsonArray array, JsonAnimator animator) {
        super(array, animator);
    }

    @Override
    protected void performAnimation(TabulaModel model, Entity entity, Info info, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        if(entity instanceof DinosaurEntity) {
            DinosaurEntity dino = (DinosaurEntity)entity;
            if(dino.getLegSolver() instanceof LegSolverQuadruped) {
                LegArticulator.articulateQuadruped(dino, (LegSolverQuadruped)dino.getLegSolver(), model.getCube(info.root), model.getCube(info.neck),
                        model.getCube(info.backLeftThigh), model.getCube(info.backLeftCalf), model.getCube(info.backRightThigh), model.getCube(info.backRightCalf),
                        model.getCube(info.frontLeftThigh), model.getCube(info.frontLeftCalf), model.getCube(info.frontRightThigh), model.getCube(info.frontRightCalf),
                        info.rotBackThigh, info.rotBackCalf, info.rotFrontThigh, info.rotFrontCalf, Minecraft.getMinecraft().getRenderPartialTicks());
            }
        }
    }

    @Override
    public Info createValue(JsonObject json, JsonAnimator animator) {
        return new Info(json);
    }

    public static class Info {

        private final String root;
        private final String neck;
        private final String backLeftThigh;
        private final String backLeftCalf;
        private final String backRightThigh;
        private final String backRightCalf;
        private final String frontLeftThigh;
        private final String frontLeftCalf;
        private final String frontRightThigh;
        private final String frontRightCalf;

        private final float rotBackThigh;
        private final float rotBackCalf;
        private final float rotFrontThigh;
        private final float rotFrontCalf;


        public Info(JsonObject json) {
            this.root = JsonUtils.getString(json, "root");
            this.neck = JsonUtils.getString(json, "neck");
            this.backLeftThigh = JsonUtils.getString(json, "back_left_thigh");
            this.backLeftCalf = JsonUtils.getString(json, "back_left_calf");
            this.backRightThigh = JsonUtils.getString(json, "back_right_thigh");
            this.backRightCalf = JsonUtils.getString(json, "back_right_calf");
            this.frontLeftThigh = JsonUtils.getString(json, "front_left_thigh");
            this.frontLeftCalf = JsonUtils.getString(json, "front_left_calf");
            this.frontRightThigh = JsonUtils.getString(json, "front_right_thigh");
            this.frontRightCalf = JsonUtils.getString(json, "front_right_calf");

            this.rotBackThigh = JsonUtils.getFloat(json, "rotation_back_thigh");
            this.rotBackCalf = JsonUtils.getFloat(json, "rotation_back_calf");
            this.rotFrontThigh = JsonUtils.getFloat(json, "rotation_front_thigh");
            this.rotFrontCalf = JsonUtils.getFloat(json, "rotation_front_calf");

        }
    }
}
