package org.jurassicraft.server.dinosaur;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.MicroraptorEntity;
import org.jurassicraft.server.food.FoodType;
import org.jurassicraft.server.period.TimePeriod;

public class MicroraptorDinosaur extends Dinosaur {
    public MicroraptorDinosaur() {
        super();
        this.setName("Microraptor");
        this.setDinosaurClass(MicroraptorEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x142146, 0x101625);
        this.setEggColorFemale(0x0E1423, 0x121827);
        this.setSpeed(0.15, 0.20);
        this.setAttackSpeed(1.3);
        this.setHealth(4, 10);
        this.setStrength(0.5, 2);
        this.setMaximumAge(this.fromDays(30));
        this.setFlee(true);
        this.setEyeHeight(0.2F, 0.5F);
        this.setSizeX(0.2F, 0.7F);
        this.setSizeY(0.25F, 0.6F);
        this.setStorage(9);
        this.setDiet(new Diet().withModule(new Diet.DietModule(FoodType.INSECT)).withModule(new Diet.DietModule(FoodType.MEAT)));
        this.setBones("tooth", "arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.4F, 0.15F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(16);
        this.setAttackBias(400.0);
        this.setCanClimb(true);
        this.setBreeding(false, 1, 5, 15, false, true);
        this.setJumpHeight(2);
        this.setRandomFlock(false);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DENSE));
        this.setSpawn(10, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
