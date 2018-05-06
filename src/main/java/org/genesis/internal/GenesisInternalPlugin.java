package org.genesis.internal;

import org.genesis.Genesis;
import org.genesis.api.GenesisPlugin;
import org.genesis.api.IGenesisPlugin;
import org.genesis.api.IGenesisRegistry;

@GenesisPlugin
public class GenesisInternalPlugin implements IGenesisPlugin {

    @Override
    public void register(IGenesisRegistry registry) {
	
    }
    
    @Override
    public String getModID() {
	return Genesis.MODID;
    }


}
