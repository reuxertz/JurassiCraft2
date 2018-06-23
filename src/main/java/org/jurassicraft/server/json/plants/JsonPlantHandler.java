package org.jurassicraft.server.json.plants;

import com.google.gson.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.json.JsonUtil;
import org.jurassicraft.server.plant.Plant;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class JsonPlantHandler {

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Plant.class, (JsonDeserializer)(jsonElement, type, context) -> {
        JsonObject json = jsonElement.getAsJsonObject();
        String blockName = JsonUtils.getString(json, "block");
        return new Plant(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)), "Could not find block " + blockName), JsonUtils.getInt(json, "heal_amount"))
                .withIsPrehistoric(JsonUtils.getBoolean(json, "prehistoric", true))
                .withTreeType(JsonUtils.isString(json, "tree_type") ? TreeType.valueOf(JsonUtils.getString(json, "tree_type").toUpperCase(Locale.ENGLISH)) : null)
                .withFoodEffects(getFoodEffects(json));
    }).create();

    private static FoodHelper.FoodEffect[] getFoodEffects(JsonObject json) {
        if(!json.has("food_effects")) {
            return new FoodHelper.FoodEffect[0];
        }
        JsonArray jsonArray = JsonUtils.getJsonArray(json, "food_effects");
        FoodHelper.FoodEffect[] array = new FoodHelper.FoodEffect[jsonArray.size()];
        int i = 0;
        for (JsonElement element : jsonArray) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject potionObject = obj.getAsJsonObject("potion");
            String potionName = JsonUtils.getString(potionObject, "potion_name");
            array[i] = new FoodHelper.FoodEffect(new PotionEffect(Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionName)), "Could not find potion " + potionName),
                    JsonUtils.getInt(potionObject, "duration", 0),
                    JsonUtils.getInt(potionObject, "amplifier", 0),
                    JsonUtils.getBoolean(potionObject, "ambient", false),
                    JsonUtils.getBoolean(potionObject, "show_particles", true)
            ),
            JsonUtils.getInt(obj, "chance"));
        }
        return array;
    }


    @SubscribeEvent
    public static void onPlantRegistry(RegistryEvent.Register<Plant> event) {
        JsonUtil.getAllRegister(event.getRegistry(), GSON, "plants");
    }

}
