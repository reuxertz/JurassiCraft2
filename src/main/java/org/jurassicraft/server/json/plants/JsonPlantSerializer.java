package org.jurassicraft.server.json.plants;

import com.google.gson.*;
import net.minecraft.potion.PotionEffect;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.plant.Plant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class JsonPlantSerializer {

    public static JsonElement serilizePlant(Plant plant) {
        JsonObject parent = new JsonObject();
        parent.addProperty("block", plant.getBlock().getRegistryName().toString());
        parent.addProperty("heal_amount", plant.getHealAmount());
        if(!plant.isPrehistoric()) {
            parent.addProperty("prehistoric", false);
        }
        if(plant.getTreeType() != null) {
            parent.addProperty("tree_type", plant.getTreeType().name().toLowerCase(Locale.ENGLISH));
        }
        if(plant.getEffects().length != 0) {
            JsonArray foodEffects = new JsonArray();
            for (FoodHelper.FoodEffect foodEffect : plant.getEffects()) {
                PotionEffect potion = foodEffect.effect;
                JsonObject potionObject = new JsonObject();
                potionObject.addProperty("potion_name", potion.getPotion().getRegistryName().toString());
                if(potion.getDuration() != 0) {
                    potionObject.addProperty("duration", potion.getDuration());
                }
                if(potion.getAmplifier() != 0) {
                    potionObject.addProperty("amplifier", potion.getAmplifier());
                }
                if(potion.getIsAmbient()) {
                    potionObject.addProperty("ambient", true);
                }
                if(!potion.doesShowParticles()) {
                    potionObject.addProperty("show_particles", false);
                }

                JsonObject object = new JsonObject();
                object.add("potion", potionObject);
                object.addProperty("chance", foodEffect.chance);
                foodEffects.add(object);
            }
            parent.add("food_effects", foodEffects);
        }
        return parent;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void writeToFile(Plant plant, File folder) {
        if(!folder.isDirectory()) {
            throw new IllegalArgumentException("Expected folder, found file");
        }
        folder.mkdirs();
        File file = new File(folder, plant.getRegistryName().getResourcePath() + ".json");
        if(file.exists()) {
            file.delete();
        }
        try (FileWriter w = new FileWriter(file)) {
            GSON.toJson(serilizePlant(plant), w);
        } catch (IOException e) {
            e.printStackTrace();
        }    }

}
