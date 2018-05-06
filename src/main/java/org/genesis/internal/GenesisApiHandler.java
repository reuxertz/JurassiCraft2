package org.genesis.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.genesis.Genesis;
import org.genesis.api.GenesisPlugin;
import org.genesis.api.IGenesisPlugin;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

public class GenesisApiHandler {
    public static final ArrayList<IGenesisPlugin> PLUGINS = Lists.newArrayList();

    public static void loadPlugins(ASMDataTable table) {
	PLUGINS.clear(); //Should be empty anyway, but just to make sure plugins arn't double registered
	String annotationClassName = GenesisPlugin.class.getCanonicalName();
	Set<ASMDataTable.ASMData> asmDatas = table.getAll(annotationClassName);
	for (ASMDataTable.ASMData asmData : asmDatas) {
	    try {
		Class<?> asmClass = Class.forName(asmData.getClassName());
		Class<? extends IGenesisPlugin> asmInstanceClass = asmClass.asSubclass(IGenesisPlugin.class);
		IGenesisPlugin instance = asmInstanceClass.newInstance();
		PLUGINS.add(instance);
	    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | LinkageError e) {
		Genesis.getLogger().error("Failed to load plugin {}: {}", asmData.getClassName(), e.getLocalizedMessage());
	    }
	}
    }
    public static void register() {
	//Clear all current data. 
	
	for(IGenesisPlugin plugin : PLUGINS) {
	    long time = System.currentTimeMillis();
	    Genesis.getLogger().info("Sending registry event to: {}" , plugin.getModID());
	    try {
		plugin.register(new GenesisRegistry(plugin.getModID()));
	    } catch (Throwable t) {
		throw new RuntimeException("Exception while loading Genesis plugin :" + plugin.getModID(), t);
	    }
	    Genesis.getLogger().info("Successfully registered Genesis plugin: {} (took {}ms)" , plugin.getModID(), System.currentTimeMillis() - time);

	}
    }
}
