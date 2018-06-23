package org.jurassicraft.server.dinosaur;

import java.util.ArrayList;

import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.TriceratopsEntity;
import org.jurassicraft.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class TriceratopsDinosaur extends Dinosaur {
    public TriceratopsDinosaur() {
        super();

        this.setName("Triceratops");
        this.setDinosaurClass(TriceratopsEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x404138, 0x1C1C1C);
        this.setEggColorFemale(0x8F7B76, 0x73676A);
        this.setSpeed(0.3, 0.35);
        this.setAttackSpeed(1.3);
        this.setHealth(10, 70);
        this.setStrength(2, 10);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.45F, 1.8F);
        this.setSizeX(0.35F, 2.5F);
        this.setSizeY(0.6F, 3.0F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "horn", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.325F);
        this.setOffset(0.0F, 0.45F, 0.0F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setAttackBias(400.0);
        this.setBreeding(BirthType.EGG_LAYING, 2, 6, 48, false, true);
        String[][] recipe = {
                {"", "", "","","horn"},
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder_bone", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.setSpawn(10, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.FOREST);
    }
}
