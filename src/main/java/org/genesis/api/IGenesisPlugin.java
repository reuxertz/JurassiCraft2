package org.genesis.api;

/**
 * The main class to implement to make a plugin with Genesis. Genesis only communicates with other mods through this class.
 * The class must have a constructor with no arguments and have to have the annotation {@link IGenesisPlugin} to be loaded up.
 * @author Wyn Price
 *
 */
public interface IGenesisPlugin {

    /**
     * Where all the registering is done. Genesis will call only this method while registering things.
     * @param registry The Registry used to register things.
     */
    void register(IGenesisRegistry registry);
    
    /**
     * The modid of the mod youre making a plugin for. Used to help identify problems
     * @return Your mod's modID.
     */
    String getModID();
}
