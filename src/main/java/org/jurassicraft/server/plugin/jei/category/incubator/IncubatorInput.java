package org.jurassicraft.server.plugin.jei.category.incubator;

import org.jurassicraft.server.entity.Dinosaur;

public class IncubatorInput {

    private final Dinosaur dinosaur;

    public IncubatorInput(Dinosaur dinosaur) {
        this.dinosaur = dinosaur;
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

}