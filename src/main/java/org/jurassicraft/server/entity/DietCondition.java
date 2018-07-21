package org.jurassicraft.server.entity;

import com.google.common.collect.Lists;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;

import java.util.List;
import java.util.function.Predicate;


public class DietCondition {

    private static final List<DietCondition> conditions = Lists.newArrayList();

    public static final DietCondition INFANT = new DietCondition("infant", entity -> entity.getAgePercentage() < 25);

    public static DietCondition getCondition(String name) {
        for (DietCondition condition : conditions) {
            if(condition.getName().equalsIgnoreCase(name)) {
                return condition;
            }
        }
        return null;
    }

    private final String name;
    private final Predicate<DinosaurEntity> predicate;

    DietCondition(String name, Predicate<DinosaurEntity> preticate) {
        this.name = name;
        this.predicate = preticate;

        if(getCondition(name) != null) {
            throw new IllegalArgumentException("Condition with name " + name + " has already been registered");
        }
        conditions.add(this);
    }

    public String getName() {
        return name;
    }

    public boolean apply(DinosaurEntity dinosaurEntity) {
        return this.predicate.test(dinosaurEntity);
    }
}
