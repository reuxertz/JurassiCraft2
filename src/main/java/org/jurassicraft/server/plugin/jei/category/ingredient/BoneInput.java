package org.jurassicraft.server.plugin.jei.category.ingredient;

import org.jurassicraft.server.dinosaur.Dinosaur;

public class BoneInput {
    private final Dinosaur dinosaur;
    private final String bone;

    public BoneInput(Dinosaur dinosaur, String bone) {
        this.dinosaur = dinosaur;
        this.bone = bone;
    }
    
    public Dinosaur getDinosaur() {
	return dinosaur;
    }
    
    public String getBone() {
	return bone;
    }
}
