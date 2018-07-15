//package org.jurassicraft.server.dinosaur;
//
//import java.util.ArrayList;
//
//import org.jurassicraft.server.entity.Diet;
//import org.jurassicraft.server.entity.SleepTime;
//import org.jurassicraft.server.entity.ai.util.MovementType;
//import org.jurassicraft.server.entity.dinosaur.AlligatorGarEntity;
//import org.jurassicraft.server.food.FoodType;
//import org.jurassicraft.server.period.TimePeriod;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.MobEffects;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.world.biome.Biome;
//import net.minecraftforge.common.BiomeDictionary;
//
//public class AlligatorGarDinosaur extends Dinosaur
//{
//    public AlligatorGarDinosaur()
//    {
//        super();
//        this.setName("Alligator Gar");
//        this.setDinosaurClass(AlligatorGarEntity.class);
//        this.setDinosaurBehaviourType(DinosaurBehaviourType.PASSIVE);
//        this.setTimePeriod(TimePeriod.CRETACEOUS);
//        this.setEggColorMale(0x707B94, 0x3B4963);
//        this.setEggColorFemale(0x7C775E, 0x4D4A3B);
//        this.setHealth(3, 10);
//        this.setFlee(true);
//        this.setSpeed(0.2, 0.40);
//        this.setAttackSpeed(1.5);
//        this.setStrength(0.5, 3);
//        this.setMaximumAge(this.fromDays(30));
//        this.setEyeHeight(0.35F, 1.2F);
//        this.setSizeX(0.1F, 1.1F);
//        this.setSizeY(0.02F, .4F);
//        this.setStorage(9);
//        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
//        this.setSleepTime(SleepTime.NO_SLEEP);
//        this.setBones("anal_fin", "caudal_fin", "first_dorsal_fin", "pectoral_fin_bones", "pelvic_fin_bones", "second_dorsal_fin", "skull", "spine", "teeth");
//        this.setHeadCubeName("Head");
//        this.setScale(.65F, 0.15F);
//        this.setMaxHerdSize(1);
//        this.setOffset(0.0F, .7F, 0F);
//        this.setAttackBias(100.0);
//        this.setMarineAnimal(true);
//        this.setMovementType(MovementType.NEAR_SURFACE);
//        this.setBreeding(BirthType.LIVE_BIRTH, 1, 3, 15, true, false);
//        this.setRandomFlock(false);
//        String[][] recipe = {{"", "second_dorsal_fin", "first_dorsal_fin", ""},
//                {"caudal_fin", "spine", "pectoral_fin_bones", "skull"},
//                {"anal_fin", "", "pelvic_fin_bones", "teeth"}};
//        this.setRecipe(recipe);
//        this.setShadowSize(0F);
//        this.setAnimatorClassName("org.jurassicraft.client.model.animation.entity.AlligatorGarAnimator");
//        this.setSpawn(5, BiomeDictionary.Type.OCEAN);
//    }
//
//    @Override
//    public void applyMeatEffect(EntityPlayer player, boolean cooked)
//    {
//        if (!cooked)
//        {
//            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 1));
//        }
//        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 1));
//    }
//}
