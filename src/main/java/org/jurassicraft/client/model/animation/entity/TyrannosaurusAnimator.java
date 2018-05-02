package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.client.model.animation.EntityAnimator;
import org.jurassicraft.server.entity.dinosaur.TyrannosaurusEntity;

@SideOnly(Side.CLIENT)
public class TyrannosaurusAnimator extends EntityAnimator<TyrannosaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, TyrannosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer waist = model.getCube("bodyHips");
        AdvancedModelRenderer stomach = model.getCube("bodyBack");
        AdvancedModelRenderer chest = model.getCube("bodyShoulders");

        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer neck5 = model.getCube("neck5");
        AdvancedModelRenderer neck6 = model.getCube("neck6");
        AdvancedModelRenderer neck7 = model.getCube("neck7");
        AdvancedModelRenderer neck8 = model.getCube("neck8");

        AdvancedModelRenderer head = model.getCube("head");

        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");
        AdvancedModelRenderer tail6 = model.getCube("tail6");
        AdvancedModelRenderer tail7 = model.getCube("tail7");
        AdvancedModelRenderer tail8 = model.getCube("tail8");
        AdvancedModelRenderer tail9 = model.getCube("tail9");

        AdvancedModelRenderer handLeft = model.getCube("handLeft");
        AdvancedModelRenderer lowerArmLeft = model.getCube("forearmLeft");

        AdvancedModelRenderer handRight = model.getCube("handRight");
        AdvancedModelRenderer lowerArmRight = model.getCube("forearmRight");

        AdvancedModelRenderer leftThigh = model.getCube("thighLeft");
        AdvancedModelRenderer rightThigh = model.getCube("thighRight");

        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail9, tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { head, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck1, chest, stomach, waist };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft };
        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight };

        float delta = Minecraft.getMinecraft().getRenderPartialTicks();
        AdvancedModelRenderer leftCalf = model.getCube("calfLeft");
        AdvancedModelRenderer rightCalf = model.getCube("calfRight");
        LegArticulator.articulateBiped(entity, entity.legSolver, waist, leftThigh, leftCalf, rightThigh, rightCalf, 0.4F, 0.4F, delta);

        float globalSpeed = 0.5F;
        float globalDegree = 0.5F;

        model.bob(waist, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tailParts, globalSpeed * 0.5F, globalDegree * 0.05F, 1, f, f1);
        model.chainWave(bodyParts, globalSpeed * 0.5F, globalDegree * 0.025F, 3, f, f1);

        model.chainWave(bodyParts, 0.1F, -0.03F, 3, ticks, 0.25F);
        model.chainWave(rightArmParts, -0.1F, 0.2F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, -0.1F, 0.2F, 4, ticks, 0.25F);
        model.chainWave(tailParts, 0.1F, -0.1F, 2, ticks, 0.1F);

        model.faceTarget(rotationYaw, rotationPitch, 1.5F, chest, neck1, neck5, head);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
