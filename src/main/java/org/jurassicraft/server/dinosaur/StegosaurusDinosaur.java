package org.jurassicraft.server.dinosaur;

import java.util.ArrayList;

import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.StegosaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class StegosaurusDinosaur extends Dinosaur{
    public StegosaurusDinosaur() {
        super();

        this.setName("Stegosaurus");
        this.setDinosaurClass(StegosaurusEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x404138, 0x1C1C1C);
        this.setEggColorFemale(0x8F7B76, 0x73676A);
        this.setSpeed(0.3, 0.35);
        this.setAttackSpeed(1.3);
        this.setHealth(10, 70);
        this.setStrength(2, 10);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.45F, 1.8F);
        this.setSizeX(3.0F, 4.0F);
        this.setSizeY(2.0F, 3.5F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "horn", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.325F);
        this.setOffset(0.0F, 0.45F, 0.0F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(15);
        this.setAttackBias(400.0);
        this.setBreeding(BirthType.EGG_LAYING, 2, 6, 48, false, true);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SPARSE));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(5, biomeList.toArray(new Biome[biomeList.size()]));
    }
}

