package org.jurassicraft.server.json.dinosaur.objects;

import org.jurassicraft.server.entity.DinosaurEntity;

import java.util.function.Function;


public enum DietConditionType {
    INFANT(entity -> entity.getAgePercentage() < 25)
    ;

    private final Function<DinosaurEntity, Boolean> function;

    DietConditionType(Function<DinosaurEntity, Boolean> function) {
        this.function = function;
    }

    public Function<DinosaurEntity, Boolean> getFunction() {
        return function;
    }
}
