//package org.jurassicraft.server.dinosaur;
//
//import net.minecraft.world.biome.Biome;
//import net.minecraftforge.common.BiomeDictionary;
//
//import java.util.ArrayList;
//
//import org.jurassicraft.server.entity.Diet;
//import org.jurassicraft.server.entity.dinosaur.ParasaurolophusEntity;
//import org.jurassicraft.server.period.TimePeriod;
//
//public class ParasaurolophusDinosaur extends Dinosaur {
//    public ParasaurolophusDinosaur() {
//        super();
//
//        this.setName("Parasaurolophus");
//        this.setDinosaurClass(ParasaurolophusEntity.class);
//        this.setDinosaurBehaviourType(DinosaurBehaviourType.PASSIVE);
//        this.setTimePeriod(TimePeriod.CRETACEOUS);
//        this.setEggColorMale(0x9F8138, 0x422306);
//        this.setEggColorFemale(0x5F653E, 0x3C3F44);
//        this.setHealth(10, 55);
//        this.setSpeed(0.35, 0.41);
//        this.setStrength(2, 8);
//        this.setMaximumAge(this.fromDays(45));
//        this.setEyeHeight(0.45F, 2.45F);
//        this.setSizeX(0.5F, 2.5F);
//        this.setSizeY(0.8F, 3.5F);
//        this.setOffset(0.0F, 0.0F, 0.6F);
//        this.setStorage(36);
//        this.setDiet(Diet.HERBIVORE.get());
//        this.setBones("ribcage", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "shoulder_bone", "skull", "tail_vertebrae", "teeth");
//        this.setHeadCubeName("Head");
//        this.setScale(1.6F, 0.4F);
//        this.setImprintable(true);
//        this.setFlockSpeed(1.5F);
//        this.setAttackBias(-100.0);
//        this.setBreeding(BirthType.EGG_LAYING, 4, 6, 40, false, true);
//
//        String[][] recipe = {
//                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
//                {"hind_leg_bones", "hind_leg_bones", "", "shoulder_bone", "teeth"},
//                {"", "", "", "front_leg_bones", "front_leg_bones"}};
//        this.setRecipe(recipe);
//        this.setShadowSize(0.65F);
//        this.setAnimatorClassName("org.jurassicraft.client.model.animation.entity.ParasaurolophusAnimator");
//        this.setSpawn(15, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST);
//    }
//}
