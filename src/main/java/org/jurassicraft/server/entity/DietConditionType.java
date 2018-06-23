package org.jurassicraft.server.entity;

import java.util.function.Predicate;


public enum DietConditionType implements Predicate<DinosaurEntity> {
    INFANT(entity -> entity.getAgePercentage() < 25)
    ;

    private final Predicate<DinosaurEntity> predicate;

    DietConditionType(Predicate<DinosaurEntity> preticate) {
        this.predicate = preticate;
    }

    @Override
    public boolean test(DinosaurEntity dinosaurEntity) {
        return this.predicate.test(dinosaurEntity);
    }
}
