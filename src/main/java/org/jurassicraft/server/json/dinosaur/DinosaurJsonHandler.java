package org.jurassicraft.server.json.dinosaur;

import com.google.gson.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.json.JsonUtil;
import org.jurassicraft.server.json.dinosaur.entity.objects.*;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurModel;
import org.jurassicraft.server.json.dinosaur.model.objects.Constants;
import org.jurassicraft.server.json.dinosaur.model.objects.JsonAnimationType;
import org.jurassicraft.server.json.dinosaur.objects.*;

import java.lang.reflect.Type;
@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
@SuppressWarnings("unused")
public class DinosaurJsonHandler {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Dinosaur.class, new JsonHandler())
            .registerTypeAdapter(AdultBabyValue.class, new AdultBabyValue.JsonHandler())
            .registerTypeAdapter(DinosaurBreeding.class, new DinosaurBreeding.JsonHandler())
            .registerTypeAdapter(DinosaurProperties.class, new DinosaurProperties.JsonHandler())
            .registerTypeAdapter(DinosaurSpawningInfo.class, new DinosaurSpawningInfo.JsonHandler())
            .registerTypeAdapter(DinosaurStatistics.class, new DinosaurStatistics.JsonHandler())
            .registerTypeAdapter(DinosaurTraits.class, new DinosaurTraits.JsonHandler())
            .registerTypeAdapter(Diet.class, new JsonDiet())
            .registerTypeAdapter(SpawnEggInfo.class, new SpawnEggInfo.JsonHandler())

            //Model Stuff
            .registerTypeAdapter(JsonDinosaurModel.class, new JsonDinosaurModel.Deserializer())
            .registerTypeAdapter(JsonAnimator.class, new JsonAnimator.Deserializer())
            .registerTypeAdapter(Constants.class, new Constants.Deserializer())
            .registerTypeAdapter(JsonAnimationType.class, new JsonAnimationType.Deserializer())

            //Entity Stuff
            .registerTypeAdapter(AttackTargets.class, new AttackTargets.Deserializer())
            .registerTypeAdapter(EntityAiBlock.class, new EntityAiBlock.Deserializer())
            .registerTypeAdapter(EntityAiBlockNoPriority.class, new EntityAiBlockNoPriority.Deserializer())
            .registerTypeAdapter(EntityJsonAi.class, new EntityJsonAi.Deserializer())
            .registerTypeAdapter(EntityJsonAttributes.class, new EntityJsonAttributes.Deserializer())
            .registerTypeAdapter(EntityJsonSounds.class, new EntityJsonSounds.Deserializer())
            .registerTypeAdapter(EntityProperties.class, new EntityProperties.Deserializer())
            .create();

    @SubscribeEvent
    public static void onDinosaurRegistry(RegistryEvent.Register<Dinosaur> event) {
        JsonUtil.getAllRegister(event.getRegistry(), GSON, "dinosaurs");

//        event.getRegistry().register(new StegosaurusDinosaur().setRegistryName("stegosaurus"));

//        if(Boolean.FALSE) { //Debug stuff. Dont use unless you know what youre doing
//            File folder = new File(new File(".").getAbsoluteFile().getParentFile().getParentFile(), "src\\main\\resources\\assets\\jurassicraft\\jurassicraft\\dinosaurs");
//            for (Dinosaur dinosaur : new Dinosaur[]{
//                    new BrachiosaurusDinosaur(),
//                    new CoelacanthDinosaur(),
//                    new DilophosaurusDinosaur(),
//                    new GallimimusDinosaur(),
//                    new MicroraptorDinosaur(),
//                    new MussaurusDinosaur(),
//                    new ParasaurolophusDinosaur(),
//                    new TriceratopsDinosaur(),
//                    new TyrannosaurusDinosaur(),
//                    new VelociraptorDinosaur()
//            }) {
//                try(FileWriter fw = new FileWriter(new File(folder, dinosaur.getName().toLowerCase() + ".json"))) {
//                    GSON.toJson(dinosaur, Dinosaur.class, fw);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public static class JsonHandler implements JsonDeserializer<Dinosaur>, JsonSerializer<Dinosaur> {

        @Override
        public Dinosaur deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new JsonDinosaur(context.deserialize(json, DinosaurProperties.class));
        }

        @Override
        public JsonElement serialize(Dinosaur dino, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(new DinosaurProperties(
                    dino.getName(),
                    dino.getTimePeriod(),
                    dino.getHeadCubeName(),
                    dino.getAnimatorClassName() != null ? dino.getAnimatorClassName() : "", //TODO: remove this line when all dinosaur animators are turned into json
                    dino.getAnimatorClassName() != null ? "" : dino.getRegistryName().toString(),
                    dino.getShadowSize(),
                    new SpawnEggInfo(dino.getPrimaryEggColorMale(), dino.getSecondaryEggColorMale()),
                    new SpawnEggInfo(dino.getPrimaryEggColorFemale(), dino.getSecondaryEggColorFemale()),
                    new DinosaurStatistics(
                            new AdultBabyValue(dino.getBabySpeed(),     dino.getAdultSpeed()),
                            new AdultBabyValue(dino.getBabyHealth(),    dino.getAdultHealth()),
                            new AdultBabyValue(dino.getBabyStrength(),  dino.getAdultStrength()),
                            new AdultBabyValue(dino.getBabySizeX(),     dino.getAdultSizeX()),
                            new AdultBabyValue(dino.getBabySizeY(),     dino.getAdultSizeY()),
                            new AdultBabyValue(dino.getBabyEyeHeight(), dino.getAdultEyeHeight()),
                            new AdultBabyValue(dino.getScaleInfant(),   dino.getScaleAdult()),
                            dino.getJumpHeight(),
                            dino.getAttackSpeed(),
                            dino.getStorage()
                    ),
                    new DinosaurTraits(
                            dino.getHomeType(),
                            dino.getDinosaurBehaviourType(),
                            dino.getDiet(),
                            dino.getSleepTime(),
                            dino.isImprintable(),
                            dino.isDefendOwner(),
                            dino.getMaximumAge(),
                            dino.getMaxHerdSize(),
                            dino.getAttackBias(),
                            dino.isCanClimb(),
                            dino.getFlockSpeed()
                    ),
                    new DinosaurSpawningInfo(dino.getSpawnChance(), dino.getBiomeTypes()),
                    new DinosaurBreeding(
                            dino.getBirthType(),
                            dino.getMinClutch(),
                            dino.getMaxClutch(),
                            dino.getBreedCooldown(),
                            dino.isBreedAroundOffspring(),
                            dino.isDefendOffspring()
                    ),
                    dino.getBones(),
                    dino.getRecipe()
            ));
        }
    }
}
