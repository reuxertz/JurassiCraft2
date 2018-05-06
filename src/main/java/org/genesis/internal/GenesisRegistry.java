package org.genesis.internal;

import org.genesis.api.IGenesisRegistry;

public class GenesisRegistry implements IGenesisRegistry {
    private final String modid; 
    
    public GenesisRegistry(String modid) {
	this.modid = modid;
    }
}
