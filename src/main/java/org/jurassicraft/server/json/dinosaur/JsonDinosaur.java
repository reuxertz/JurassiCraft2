package org.jurassicraft.server.json.dinosaur;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.json.dinosaur.objects.*;

public class JsonDinosaur extends Dinosaur {

    public JsonDinosaur(DinosaurProperties properties) {
        this.setName(properties.getName());
        this.setTimePeriod(properties.getTimePeriod());
        this.setHeadCubeName(properties.getHeadCubeName());
        this.setAnimatorClassName(properties.getDinosaurAnimatorClassName());

        SpawnEggInfo male = properties.getMaleSpawnEgg();
        this.setPrimaryEggColorMale(male.getPrimary());
        this.setSecondaryEggColorMale(male.getSecondary());

        SpawnEggInfo female = properties.getFemaleSpawnEgg();
        this.setPrimaryEggColorFemale(female.getPrimary());
        this.setSecondaryEggColorFemale(female.getSecondary());

        DinosaurStatistics statistics = properties.getStatistics();
        statistics.getSpeed().apply(this::setBabySpeed, this::setAdultSpeed);
        statistics.getHealth().apply(this::setBabyHealth, this::setAdultHealth);
        statistics.getStrength().apply(this::setBabyStrength, this::setAdultStrength);
        statistics.getSizeX().apply(this::setBabySizeX, this::setAdultSizeX);
        statistics.getSizeY().apply(this::setBabySizeY, this::setAdultSizeY);
        statistics.getEyeHeight().apply(this::setBabyEyeHeight, this::setAdultEyeHeight);
        statistics.getScale().apply(this::setScaleInfant, this::setScaleAdult);
        this.setJumpHeight(statistics.getJumpHeight());
        this.setAttackSpeed(statistics.getAttackSpeed());
        this.setStorage(statistics.getItemStorage());

        DinosaurTraits traits = properties.getTraits();
        this.setDinosaurBehaviourType(traits.getType());
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
