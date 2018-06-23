package org.jurassicraft.server.entity;

import java.util.function.Predicate;


public enum DietConditionType {
    INFANT(entity -> entity.getAgePercentage() < 25)
    ;

    private final Predicate<DinosaurEntity> predicate;

    DietConditionType(Predicate<DinosaurEntity> preticate) {
        this.predicate = preticate;
    }

    public boolean apply(DinosaurEntity dinosaurEntity) {
        return this.predicate.test(dinosaurEntity);
    }
}
