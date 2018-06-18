package org.jurassicraft.server.plant;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(JurassiCraft.MODID)
@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class PlantHandler {
    public static final Plant AJUGINUCULA_SMITHII = null;
    public static final Plant SMALL_ROYAL_FERN = null;
    public static final Plant CALAMITES = null;
    public static final Plant SMALL_CHAIN_FERN = null;
    public static final Plant SMALL_CYCAD = null;
    public static final Plant GINKGO = null;
    public static final Plant BENNETTITALEAN_CYCADEOIDEA = null;
    public static final Plant CRY_PANSY = null;
    public static final Plant SCALY_TREE_FERN = null;
    public static final Plant CYCAD_ZAMITES = null;
    public static final Plant DICKSONIA = null;
    public static final Plant WILD_ONION = null;
    public static final Plant DICROIDIUM_ZUBERI = null;
    public static final Plant DICTYOPHYLLUM = null;
    public static final Plant WEST_INDIAN_LILAC = null;
    public static final Plant SERENNA_VERIFORMANS = null;
    public static final Plant LADINIA_SIMPLEX = null;
    public static final Plant ORONTIUM_MACKII = null;
    public static final Plant UMALTOLEPIS = null;
    public static final Plant LIRIODENDRITES = null;
    public static final Plant RAPHAELIA = null;
    public static final Plant ENCEPHALARTOS = null;
    public static final Plant PSARONIUS = null;
    public static final Plant PHOENIX = null;
    public static final Plant WILD_POTATO = null;
    public static final Plant ARAUCARIA = null;
    public static final Plant BRISTLE_FERN = null;
    public static final Plant CINNAMON_FERN = null;
    public static final Plant TEMPSKYA = null;
    public static final Plant WOOLLY_STALKED_BEGONIA = null;
    public static final Plant LARGESTIPULE_LEATHER_ROOT = null;
    public static final Plant RHACOPHYTON = null;
    public static final Plant GRAMINIDITES_BAMBUSOIDES = null;
    public static final Plant ENALLHELIA = null;
    public static final Plant AULOPORA = null;
    public static final Plant CLADOCHONUS = null;
    public static final Plant LITHOSTROTION = null;
    public static final Plant STYLOPHYLLOPSIS = null;
    public static final Plant HIPPURITES_RADIOSUS = null;
    public static final Plant HELICONIA = null;
    public static final Plant RHAMNUS_SALIFOCIFUS = null;

    public static List<Plant> getPrehistoricPlantsAndTrees() {
        return JurassicraftRegisteries.PLANT_REGISTRY.getValuesCollection().stream().filter(Plant::isPrehistoric).collect(Collectors.toList());
    }

    public static List<Plant> getPrehistoricPlants() {
        return getPrehistoricPlantsAndTrees().stream().filter(plant -> !plant.isTree()).collect(Collectors.toList());
    }
}
