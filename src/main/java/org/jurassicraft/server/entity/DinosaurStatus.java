package org.jurassicraft.server.entity;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.food.FoodType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public enum DinosaurStatus {
    CARNIVOROUS((entity, info) -> entity.getDinosaur().getDiet().canEat(entity, FoodType.MEAT)),
    PISCIVOROUS((entity, info) -> entity.getDinosaur().getDiet().canEat(entity, FoodType.FISH)),
    HERBIVOROUS((entity, info) -> entity.getDinosaur().getDiet().canEat(entity, FoodType.PLANT)),
    INSECTIVOROUS((entity, info) -> entity.getDinosaur().getDiet().canEat(entity, FoodType.INSECT)),
    DIURNAL((entity, info) -> entity.getDinosaur().getSleepTime() == SleepTime.DIURNAL),
    NOCTURNAL((entity, info) -> entity.getDinosaur().getSleepTime() == SleepTime.NOCTURNAL),
    CREPUSCULAR((entity, info) -> entity.getDinosaur().getSleepTime() == SleepTime.CREPUSCULAR),
    TAMED((entity, info) -> entity.getOwner() != null),
    LOW_HEALTH((entity, info) -> entity.getHealth() < entity.getMaxHealth() / 4 || entity.isCarcass()),
    HUNGRY((entity, info) -> info.hungry),
    THIRSTY((entity, info) -> info.thirsty),
    POISONED((entity, info) -> info.poisoned),
    DROWNING((entity, info) -> entity.getDinosaur().getHomeType() != Dinosaur.DinosaurHomeType.MARINE && entity.getAir() < 200),
    SLEEPY((entity, info) -> entity.shouldSleep()),
    FLOCKING((entity, info) -> info.flocking),
    SCARED((entity, info) -> info.scared);

    private final BiPredicate<DinosaurEntity, DinosaurEntity.FieldGuideInfo> predicate;

    DinosaurStatus(BiPredicate<DinosaurEntity, DinosaurEntity.FieldGuideInfo> predicate) {
        this.predicate = predicate;
    }

    public static List<DinosaurStatus> getActiveStatuses(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
        List<DinosaurStatus> statuses = new ArrayList<>();

        for (DinosaurStatus status : values()) {
            if (status.apply(entity, info)) {
                statuses.add(status);
            }
        }

        return statuses;
    }

    public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
        return predicate.test(entity, info);
    }
}
