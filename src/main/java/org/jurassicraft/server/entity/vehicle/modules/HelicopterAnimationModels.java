package org.jurassicraft.server.entity.vehicle.modules;

import java.util.Map;

import org.jurassicraft.server.entity.ai.util.InterpValue;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;

import com.google.common.collect.Maps;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class HelicopterAnimationModels {
    public static final Map<Class<? extends HelicopterModule>, ITabulaModelAnimator<? extends HelicopterBaseEntity>> animatorRegistry;

    static {
        animatorRegistry = Maps.newHashMap();
        animatorRegistry.put(HelicopterDoor.class, (model, entity, imbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale) -> {
//	    doorOpen.setTarget(entity.getSeat(0).getPos().distanceTo(Minecraft.getMinecraft().player.getPositionVector()) > 25D ? 0D : Math.toRadians(70));
//	    model.getCube("gunnerdoor2").rotateAngleY = (float) doorOpen.getValueForRendering(LLibrary.PROXY.getPartialTicks());
	});
    }
}
