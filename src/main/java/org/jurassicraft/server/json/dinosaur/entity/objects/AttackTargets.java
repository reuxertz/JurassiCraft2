package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import java.lang.reflect.Type;
import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AttackTargets {
    List<Class<? extends Entity>> entityList;
    List<Dinosaur> dinosaurList;

    public static class Deserializer implements JsonDeserializer<AttackTargets> {

        @Override
        @SuppressWarnings("unchecked")
        public AttackTargets deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(element, "targets");

            List<Class<? extends Entity>> entityList = Lists.newArrayList();
            List<Dinosaur> dinosaurList = Lists.newArrayList();

            for (JsonElement jsonElement : JsonUtils.getJsonArray(json, "entities")) {
                Class<?> claz;
                try {
                    claz = Class.forName(JsonUtils.getString(jsonElement, "entities"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
                if(!EntityLivingBase.class.isAssignableFrom(claz)) {
                    new IllegalArgumentException("Unable to add entity " + claz.getCanonicalName() + " as it does not extend EntityLivingBase").printStackTrace();
                    continue;
                }
                entityList.add((Class<? extends EntityLivingBase>)claz);
            }

            for (JsonElement jsonElement : JsonUtils.getJsonArray(json, "dinosaurs")) {
                dinosaurList.add(JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(JsonUtils.getString(jsonElement, "dinosaurs"))));
            }

            return new AttackTargets(entityList, dinosaurList);
        }
    }
}
