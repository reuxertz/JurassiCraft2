package org.jurassicraft.server.dinosaur;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.BrachiosaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

import java.util.ArrayList;

public class BrachiosaurusDinosaur extends Dinosaur
{
    public BrachiosaurusDinosaur()
    {
        super();

        this.setName("Brachiosaurus");
        this.setDinosaurClass(BrachiosaurusEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x87987F, 0x607343);
        this.setEggColorFemale(0xAA987D, 0x4F4538);
        this.setHealth(20, 150);
        this.setSpeed(0.2, 0.22);
        this.setStrength(5, 15);
        this.setMaximumAge(this.fromDays(85));
        this.setEyeHeight(2.2F, 18.5F);
        this.setSizeX(1F, 5F);
        this.setSizeY(2F, 5F);
        this.setStorage(54);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "hind_leg_bones", "front_leg_bones");
        this.setHeadCubeName("head");
        this.setScale(2.35F, 0.3F);
        this.setOffset(0.0F, 0.0F, 0.0F);
        this.setAttackBias(1200.0);
        this.setMaxHerdSize(4);
        //TODO Verify L48-L50 works
        //this.setSpawn(5, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));
        this.setBreeding(false, 4, 8, 72, true, false);
        String[][] recipe = {{"", "", "", "", "skull"},
                {"", "", "", "neck_vertebrae", "tooth"},
                {"tail_vertebrae", "pelvis", "ribcage", "shoulder", ""},
                {"", "hind_leg_bones", "hind_leg_bones", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);

        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(5, biomeList.toArray(new Biome[biomeList.size()]));
    }

    @Override
    public String[] getOverlays()
    {
        return new String[]{OverlayType.EYES.toString(), OverlayType.EYE_LIDS.toString(), OverlayType.CLAWS.toString(), OverlayType.MOUTH.toString(), OverlayType.NOSTRILS.toString(), OverlayType.TEETH.toString()};

    }
}
