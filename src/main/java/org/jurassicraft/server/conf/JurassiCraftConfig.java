package org.jurassicraft.server.conf;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class JurassiCraftConfig {
    @ConfigEntry(category = "Entities", name = "Dinosaur Spawning")
    public boolean naturalSpawning;

    @ConfigEntry(category = "Entities", name = "Only Hunt when Hungry")
    public boolean huntWhenHungry = false;

    @ConfigEntry(category = "Mineral Generation", name = "Fossil Generation")
    public boolean fossilGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Nest Fossil Generation")
    public boolean nestFossilGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Fossilized Trackway Generation")
    public boolean trackwayGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Plant Fossil Generation")
    public boolean plantFossilGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Amber Generation")
    public boolean amberGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Ice Shard Generation")
    public boolean iceShardGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Gypsum Generation")
    public boolean gypsumGeneration = true;

    @ConfigEntry(category = "Mineral Generation", name = "Petrified Tree Generation")
    public boolean petrifiedTreeGeneration = true;

    @ConfigEntry(category = "Plant Generation", name = "Moss Generation")
    public boolean mossGeneration = true;

    @ConfigEntry(category = "Plant Generation", name = "Peat Generation")
    public boolean peatGeneration = true;

    @ConfigEntry(category = "Plant Generation", name = "Flower Generation")
    public boolean flowerGeneration = true;

    @ConfigEntry(category = "Plant Generation", name = "Gracilaria Generation")
    public boolean gracilariaGeneration = true;

    @ConfigEntry(category = "Structure Generation", name = "Visitor Generation")
    public boolean visitorcentergeneration = true;

    @ConfigEntry(category = "Structure Generation", name = "Raptor Generation")
    public boolean raptorgeneration = true;
}
