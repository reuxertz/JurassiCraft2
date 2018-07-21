package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.Animatable;
import org.jurassicraft.server.entity.DinosaurEntity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class AnimatableModel extends FixedTabulaModel {

    public float partialTicks;
    public ResourceLocation location;

    public AnimatableModel(TabulaModelContainer model) {
        this(model, null);
    }

    public AnimatableModel(TabulaModelContainer model, ITabulaModelAnimator animator) {
        super(model, animator);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity) {
        Animatable animatable = (Animatable) entity;

        if (animatable.isCarcass()) {
            this.setMovementScale(0.0F);
        } else {
            this.setMovementScale(animatable.isSleeping() ? 0.5F : 1.0F);
        }

        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(entityIn instanceof DinosaurEntity) {
            UUID inMouthEntity = ((DinosaurEntity) entityIn).getInMouthEntity();
            UUID entityInMouth = ((DinosaurEntity) entityIn).getEntityInMouth();
            if(entityInMouth != null) {
                AdvancedModelRenderer box = this.getCube(((DinosaurEntity)entityIn).getDinosaur().jawCubeName);
                if(box instanceof FixedModelRenderer) {
                    FixedModelRenderer renderer = (FixedModelRenderer)box;
                    for (DinosaurEntity entity : entityIn.world.getEntities(DinosaurEntity.class, e -> entityInMouth.equals(e.getUniqueID()))) {
                        renderer.entityInJaw = entity;
                        renderer.partialTicks = partialTicks;
                        renderer.location = location;
                        break;
                    }
                }
            } else if(inMouthEntity != null && !entityIn.world.getEntities(DinosaurEntity.class, e -> inMouthEntity.equals(e.getUniqueID())).isEmpty() && !((DinosaurEntity)entityIn).shouldForceRender()) {
                return;
            }
        }
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public String[] getCubeIdentifierArray() {
        String[] cubeIdentifiers = new String[this.identifierMap.size()];
        int index = 0;

        Set<String> identifiers = this.identifierMap.keySet();

        for (String identifier : identifiers) {
            cubeIdentifiers[index] = identifier;
            index++;
        }

        return cubeIdentifiers;
    }

    public String[] getCubeNames() {
        String[] cubeNames = new String[this.cubes.size()];
        int index = 0;

        Set<String> names = this.cubes.keySet();

        for (String identifier : names) {
            cubeNames[index] = identifier;
            index++;
        }

        return cubeNames;
    }

    public Map<String, AdvancedModelRenderer> getIdentifierCubes() {
        return this.identifierMap;
    }

    @Override
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelRenderer... boxes) {
        float actualRotationDivisor = rotationDivisor * boxes.length;
        float yawAmount = MathHelper.clamp(MathHelper.wrapDegrees(yaw), -45.0F, 45.0F) / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = MathHelper.wrapDegrees(pitch) / (180.0F / (float) Math.PI) / actualRotationDivisor;

        for (AdvancedModelRenderer box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }
}