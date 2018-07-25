package org.jurassicraft.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.entity.item.MuralEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;

import java.util.Locale;

@GameRegistry.ObjectHolder(JurassiCraft.MODID)
@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class EntityHandler {

    public static final Dinosaur BRACHIOSAURUS = null;
    public static final Dinosaur COELACANTH = null;
    public static final Dinosaur DILOPHOSAURUS = null;
    public static final Dinosaur GALLIMIMUS = null;
    public static final Dinosaur PARASAUROLOPHUS = null;
    public static final Dinosaur MICRORAPTOR = null;
    public static final Dinosaur MUSSAURUS = null;
    public static final Dinosaur TRICERATOPS = null;
    public static final Dinosaur TYRANNOSAURUS = null;
    public static final Dinosaur VELOCIRAPTOR = null;
//    public static final Dinosaur ALLIGATORGAR = new AlligatorGarDinosaur();
    //public static final Dinosaur STEGeOSAURUS = new StegosaurusDinosaur();

    private static int entityId;


    public static void init() {
        registerEntity(AttractionSignEntity.class, "Attraction Sign");
        registerEntity(PaddockSignEntity.class, "Paddock Sign");
        registerEntity(MuralEntity.class, "Mural");
        registerEntity(VenomEntity.class, "Venom");

        registerEntity(DinosaurEntity.class, "Dinosaur");
        registerEntity(JeepWranglerEntity.class, "Jeep Wrangler");
        registerEntity(FordExplorerEntity.class, "Ford Explorer");

        registerEntity(GoatEntity.class, "Goat", 0xEFEDE7, 0x7B3E20);

        registerEntity(TranquilizerDartEntity.class, "Tranquilizer Dart");

        registerEntity(DinosaurEggEntity.class, "Dinosaur Egg");
        registerEntity(HelicopterBaseEntity.class, "Helicopter base");
    }
    
    public static void registerEntity(Class<? extends Entity> entity, String name) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        ResourceLocation registryName = new ResourceLocation("jurassicraft:entities." + formattedName);
        EntityRegistry.registerModEntity(registryName, entity, "jurassicraft." + formattedName, entityId++, JurassiCraft.INSTANCE, 1024, 1, true);
    }


    public static void registerEntity(Class<? extends Entity> entity, String name, int primary, int secondary) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        ResourceLocation registryName = new ResourceLocation("jurassicraft:entities." + formattedName);
        EntityRegistry.registerModEntity(registryName, entity, "jurassicraft." + formattedName, entityId++, JurassiCraft.INSTANCE, 1024, 1, true, primary, secondary);
    }

}
