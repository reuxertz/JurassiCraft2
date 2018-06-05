package org.jurassicraft.server.entity;

import org.jurassicraft.server.util.LangHelper;

public enum GrowthStage {
        ADULT, INFANT, JUVENILE, /*FLUORESCENT*/ADOLESCENT, SKELETON;

    // Enum#values() is not being cached for security reasons. DONT PERFORM CHANGES ON THIS ARRAY
    public static final GrowthStage[] VALUES = GrowthStage.values();

    public String getLocalization() {
        return new LangHelper("growth_stage." + this.name().toLowerCase() + ".name").build();
    }
}
