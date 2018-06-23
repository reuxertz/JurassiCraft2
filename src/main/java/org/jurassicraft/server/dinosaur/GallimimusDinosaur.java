package org.jurassicraft.server.dinosaur;

import java.util.ArrayList;

import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.DietConditionType;
import org.jurassicraft.server.entity.dinosaur.GallimimusEntity;
import org.jurassicraft.server.food.FoodType;
import org.jurassicraft.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class GallimimusDinosaur extends Dinosaur {
    public GallimimusDinosaur() {
        super();

        this.setName("Gallimimus");
        this.setDinosaurClass(GallimimusEntity.class);
        this.setDinosaurType(DinosaurType.SCARED);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD5BA86, 0xD16918);
        this.setEggColorFemale(0xCCBA94, 0xAB733D);
        this.setHealth(5, 30);
        this.setSpeed(0.2, 0.3);
        this.setStrength(1, 5);
        this.setMaximumAge(this.fromDays(35));
        this.setEyeHeight(0.58F, 2.7F);
        this.setSizeX(0.3F, 1.2F);
        this.setSizeY(0.55F, 2.25F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get().withModule(new Diet.DietModule(FoodType.INSECT).withCondition(DietConditionType.INFANT)));
        this.setBones("skull", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "foot_bones", "arm_bones");
        this.setHeadCubeName("Head Base");
        this.setScale(0.85F, 0.2F);
        this.setImprintable(true);
        this.setFlee(true);
        this.setFlockSpeed(1.4F);
        this.setBreeding(BirthType.EGG_LAYING, 2, 6, 20, false, true);
        this.setJumpHeight(3);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder",""},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.setShadowSize(0.65F);
        this.setAnimatorClassName("org.jurassicraft.client.model.animation.entity.GallimimusAnimator");
        this.setSpawn(25, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.DRY);
    }
}
