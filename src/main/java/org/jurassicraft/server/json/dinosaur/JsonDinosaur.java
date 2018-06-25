package org.jurassicraft.server.json.dinosaur;

import net.minecraft.util.ResourceLocation;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.json.dinosaur.objects.*;

public class JsonDinosaur extends Dinosaur {

    public JsonDinosaur(DinosaurProperties properties) {
        this.setName(properties.getName());
        this.setDinosaurClass(properties.getEntityClass());
        this.setTimePeriod(properties.getTimePeriod());
        this.setHeadCubeName(properties.getHeadCubeName());
        this.setAnimatorClassName(properties.getDinosaurAnimatorClassName());
        if(properties.getDinosaurModelLocation() != null) {
            this.setModelHandlerLocation(new ResourceLocation(properties.getDinosaurModelLocation()));
        }

        SpawnEggInfo male = properties.getMaleSpawnEgg();
        this.setEggColorMale(male.getPrimary(), male.getSecondary());

        SpawnEggInfo female = properties.getFemaleSpawnEgg();
        this.setEggColorFemale(female.getPrimary(), female.getSecondary());

        DinosaurStatistics statistics = properties.getStatistics();
        statistics.getSpeed().apply(this::setSpeed);
        statistics.getHealth().apply(this::setHealth);
        statistics.getStrength().apply(this::setStrength);
        statistics.getSizeX().apply(this::setSizeX);
        statistics.getSizeY().apply(this::setSizeY);
        statistics.getEyeHeight().apply(this::setEyeHeight);
        statistics.getScale().apply(this::setScale);
        this.setJumpHeight(statistics.getJumpHeight());
        this.setAttackSpeed(statistics.getAttackSpeed());
        this.setStorage(statistics.getItemStorage());

        DinosaurTraits traits = properties.getTraits();
        this.setDinosaurType(traits.getType());
        this.setDiet(traits.getDiet());
        this.setSleepTime(traits.getSleepType());
        this.setImprintable(traits.isImprintable());
        this.setDefendOwner(traits.isDefendOwner());
        this.setMaximumAge(this.fromDays(traits.getMaxAge()));
        this.setAttackBias(traits.getAttackBias());
        this.setCanClimb(traits.isCanClimb());
        this.setFlockSpeed((float)traits.getFlockSpeed());

        DinosaurSpawningInfo spawningInfo = properties.getSpawningInfo();
        this.setSpawn(spawningInfo.getChance(), spawningInfo.getBiomes());

        DinosaurBreeding breeding = properties.getBreeding();
        this.setBreeding(breeding.getBirthType(), breeding.getMinClutch(), breeding.getMaxClutch(), breeding.getBreedingCooldown(), breeding.isBreedNearOffsprring(), breeding.isDefendOffspring());

        this.setBones(properties.getBones());
        this.setRecipe(properties.getSkeletonRecipe());
    }

}
