package org.jurassicraft.server.json.dinosaur;

import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.json.dinosaur.objects.*;

public class JsonDinosaur extends Dinosaur {

	public JsonDinosaur(DinosaurProperties properties) {
		this.name = properties.name;
		this.timePeriod = properties.timePeriod;
		this.headCubeName = properties.headCubeName;
		this.animatorClassName = properties.dinosaurAnimatorClassName;

		SpawnEggInfo male = properties.maleSpawnEgg;
		this.primaryEggColorMale = male.primary;
		this.secondaryEggColorMale = male.secondary;

		SpawnEggInfo female = properties.femaleSpawnEgg;
		this.primaryEggColorFemale = female.primary;
		this.secondaryEggColorFemale = female.secondary;

		DinosaurStatistics statistics = properties.statistics;

		statistics.speed.apply(this::setBabySpeed, this::setAdultSpeed);
		statistics.health.apply(this::setBabyHealth, this::setAdultHealth);
		statistics.strength.apply(this::setBabyStrength, this::setAdultStrength);
		statistics.sizeX.apply(this::setBabySizeX, this::setAdultSizeX);
		statistics.sizeY.apply(this::setBabySizeY, this::setAdultSizeY);
		statistics.eyeHeight.apply(this::setBabyEyeHeight, this::setAdultEyeHeight);
		statistics.scale.apply(this::setScaleInfant, this::setScaleAdult);

		this.jumpHeight = statistics.jumpHeight;
		this.attackSpeed = statistics.attackSpeed;
		this.storage = statistics.itemStorage;

		DinosaurTraits traits = properties.traits;
		this.setDinosaurBehaviourType(traits.type);
		this.diet = traits.diet;
		this.sleepTime = traits.sleepType;
		this.isImprintable = traits.imprintable;
		this.defendOwner = traits.defendOwner;
		this.maximumAge = this.fromDays(traits.maxAge);
		this.attackBias = traits.attackBias;
		this.canClimb = traits.canClimb;
		this.flockSpeed = (float) traits.flockSpeed;

		DinosaurSpawningInfo spawningInfo = properties.spawningInfo;
		this.setSpawn(spawningInfo.chance, spawningInfo.biomes);

		DinosaurBreeding breeding = properties.breeding;
		this.setBreeding(breeding.birthType, breeding.minClutch, breeding.maxClutch, breeding.breedingCooldown, breeding.breedNearOffsprring, breeding.defendOffspring);

		this.bones = properties.bones;
		this.recipe = properties.skeletonRecipe;

		this.animation = properties.animation;
	}

    @Override
    public void setScaleAdult(float scaleAdult) {
        super.setScaleAdult(scaleAdult);
    }
}
