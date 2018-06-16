package org.jurassicraft.server.plant;

import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class PlantHandler {
    public static final Plant AJUGINUCULA_SMITHII = new Plant(BlockHandler.AJUGINUCULA_SMITHII, 2000).withFoodEffects(new FoodHelper.FoodEffect(new PotionEffect(MobEffects.SPEED, 100), 100));
    public static final Plant SMALL_ROYAL_FERN = new Plant(BlockHandler.SMALL_ROYAL_FERN, 2000);
    public static final Plant CALAMITES = new Plant(BlockHandler.ANCIENT_SAPLINGS.get(TreeType.CALAMITES), 1000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 5)).withTreeType(TreeType.CALAMITES);
    public static final Plant SMALL_CHAIN_FERN = new Plant(BlockHandler.SMALL_CHAIN_FERN, 2000);
    public static final Plant SMALL_CYCAD = new Plant(BlockHandler.SMALL_CYCAD, 2000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 100));
    public static final Plant GINKGO = new Plant(BlockHandler.ANCIENT_SAPLINGS.get(TreeType.GINKGO), 1000).withTreeType(TreeType.GINKGO);
    public static final Plant CYCADEOIDEA = new Plant(BlockHandler.CYCADEOIDEA, 2000);
    public static final Plant CRY_PANSY = new Plant(BlockHandler.CRY_PANSY, 250);
    public static final Plant SCALY_TREE_FERN = new Plant(BlockHandler.SCALY_TREE_FERN, 4000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 100));
    public static final Plant ZAMITES = new Plant(BlockHandler.ZAMITES, 4000);
    public static final Plant DICKSONIA = new Plant(BlockHandler.DICKSONIA, 4000);
    public static final Plant WILD_ONION = new Plant(BlockHandler.WILD_ONION, 3000);
    public static final Plant DICROIDIUM_ZUBERI = new Plant(BlockHandler.DICROIDIUM_ZUBERI, 4000);
    public static final Plant DICTYOPHYLLUM = new Plant(BlockHandler.DICTYOPHYLLUM, 2000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 15));
    public static final Plant WEST_INDIAN_LILAC = new Plant(BlockHandler.WEST_INDIAN_LILAC, 4000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.STRONG_POISON.getEffects().get(0), 100)).withIsPrehistoric(false);
    public static final Plant SERENNA_VERIFORMANS = new Plant(BlockHandler.SERENNA_VERIFORMANS, 4000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.STRONG_POISON.getEffects().get(0), 100));
    public static final Plant LADINIA_SIMPLEX = new Plant(BlockHandler.LADINIA_SIMPLEX, 2000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 15));
    public static final Plant ORONTIUM_MACKII = new Plant(BlockHandler.ORONTIUM_MACKII, 1500).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.WEAKNESS.getEffects().get(0), 50));
    public static final Plant UMALTOLEPIS = new Plant(BlockHandler.UMALTOLEPIS, 4000);
    public static final Plant LIRIODENDRITES = new Plant(BlockHandler.LIRIODENDRITES, 4000);
    public static final Plant RAPHAELIA = new Plant(BlockHandler.RAPHAELIA, 2000);
    public static final Plant ENCEPHALARTOS = new Plant(BlockHandler.ENCEPHALARTOS, 4000).withFoodEffects(new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 35));
    public static final Plant PSARONIUS = new Plant(BlockHandler.ANCIENT_SAPLINGS.get(TreeType.PSARONIUS), 1000).withTreeType(TreeType.PSARONIUS);
    public static final Plant PHOENIX = new Plant(BlockHandler.ANCIENT_SAPLINGS.get(TreeType.PHOENIX), 1000).withTreeType(TreeType.PHOENIX);
    public static final Plant WILD_POTATO = new Plant(BlockHandler.WILD_POTATO_PLANT, 3000);
    public static final Plant ARAUCARIA = new Plant(BlockHandler.ANCIENT_SAPLINGS.get(TreeType.ARAUCARIA), 1000).withTreeType(TreeType.ARAUCARIA);
    public static final Plant BRISTLE_FERN = new Plant(BlockHandler.BRISTLE_FERN, 2000);
    public static final Plant CINNAMON_FERN = new Plant(BlockHandler.CINNAMON_FERN, 2000);
    public static final Plant TEMPSKYA = new Plant(BlockHandler.TEMPSKYA, 4000);
    public static final Plant WOOLLY_STALKED_BEGONIA = new Plant(BlockHandler.WOOLLY_STALKED_BEGONIA, 2000);
    public static final Plant LARGESTIPULE_LEATHER_ROOT = new Plant(BlockHandler.LARGESTIPULE_LEATHER_ROOT, 2000);
    public static final Plant RHACOPHYTON = new Plant(BlockHandler.RHACOPHYTON, 4000);
    public static final Plant GRAMINIDITES_BAMBUSOIDES = new Plant(BlockHandler.GRAMINIDITES_BAMBUSOIDES, 4000);
    public static final Plant ENALLHELIA = new Plant(BlockHandler.ENALLHELIA, 2000);
    public static final Plant AULOPORA = new Plant(BlockHandler.AULOPORA, 2000);
    public static final Plant CLADOCHONUS = new Plant(BlockHandler.CLADOCHONUS, 2000);
    public static final Plant LITHOSTROTION = new Plant(BlockHandler.LITHOSTROTION, 2000);
    public static final Plant STYLOPHYLLOPSIS = new Plant(BlockHandler.STYLOPHYLLOPSIS, 2000);
    public static final Plant HIPPURITES_RADIOSUS = new Plant(BlockHandler.HIPPURITES_RADIOSUS, 2000);
    public static final Plant HELICONIA = new Plant(BlockHandler.HELICONIA, 4000).withIsPrehistoric(false);
    public static final Plant RHAMNUS_SALIFOCIFUS = new Plant(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT, 3000);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onRegistryEvent(RegistryEvent.Register<Plant> event) {
        event.getRegistry().registerAll(
                AJUGINUCULA_SMITHII.setRegistryName("ajuginucula_smithii"),
                SMALL_ROYAL_FERN.setRegistryName("small_royal_fern"),
                CALAMITES.setRegistryName("calamites"),
                SMALL_CHAIN_FERN.setRegistryName("small_chain_fern"),
                SMALL_CYCAD.setRegistryName("small_cycad"),
                GINKGO.setRegistryName("ginkgo"),
                CYCADEOIDEA.setRegistryName("bennettitalean_cycadeoidea"),
                CRY_PANSY.setRegistryName("cry_pansy"),
                SCALY_TREE_FERN.setRegistryName("scaly_tree_fern"),
                ZAMITES.setRegistryName("cycad_zamites"),
                DICKSONIA.setRegistryName("dicksonia"),
                WILD_ONION.setRegistryName("wild_onion"),
                DICROIDIUM_ZUBERI.setRegistryName("dicroidium_zuberi"),
                DICTYOPHYLLUM.setRegistryName("dictyophyllum"),
                WEST_INDIAN_LILAC.setRegistryName("west_indian_lilac"),
                SERENNA_VERIFORMANS.setRegistryName("serenna_veriformans"),
                LADINIA_SIMPLEX.setRegistryName("ladinia_simplex"),
                ORONTIUM_MACKII.setRegistryName("orontium_mackii"),
                UMALTOLEPIS.setRegistryName("umaltolepis"),
                LIRIODENDRITES.setRegistryName("liriodendrites"),
                RAPHAELIA.setRegistryName("raphaelia"),
                ENCEPHALARTOS.setRegistryName("encephalartos"),
                PSARONIUS.setRegistryName("psaronius"),
                PHOENIX.setRegistryName("phoenix"),
                WILD_POTATO.setRegistryName("wild_potato"),
                ARAUCARIA.setRegistryName("araucaria"),
                BRISTLE_FERN.setRegistryName("bristle_fern"),
                CINNAMON_FERN.setRegistryName("cinnamon_fern"),
                TEMPSKYA.setRegistryName("tempskya"),
                WOOLLY_STALKED_BEGONIA.setRegistryName("woolly_stalked_begonia"),
                LARGESTIPULE_LEATHER_ROOT.setRegistryName("largestipule_leather_root"),
                RHACOPHYTON.setRegistryName("rhacophyton"),
                GRAMINIDITES_BAMBUSOIDES.setRegistryName("graminidites_bambusoides"),
                ENALLHELIA.setRegistryName("enallhelia"),
                AULOPORA.setRegistryName("aulopora"),
                CLADOCHONUS.setRegistryName("cladochonus"),
                LITHOSTROTION.setRegistryName("lithostrotion"),
                STYLOPHYLLOPSIS.setRegistryName("stylophyllopsis"),
                HIPPURITES_RADIOSUS.setRegistryName("hippurites_radiosus"),
                HELICONIA.setRegistryName("heliconia"),
                RHAMNUS_SALIFOCIFUS.setRegistryName("rhamnus_salifocifus")
        );
    }

    public static List<Plant> getPrehistoricPlantsAndTrees() {
        return JurassicraftRegisteries.PLANT_REGISTRY.getValuesCollection().stream().filter(Plant::isPrehistoric).collect(Collectors.toList());
    }

    public static List<Plant> getPrehistoricPlants() {
        return getPrehistoricPlantsAndTrees().stream().filter(plant -> !plant.isTree()).collect(Collectors.toList());
    }
}
