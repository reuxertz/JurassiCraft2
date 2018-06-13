package org.jurassicraft.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.*;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.entity.item.MuralEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;

import java.util.ArrayList;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class EntityHandler {

    //TODO:
    //Automatic  registering, as in no longer supported by deafult registering

    public static final Dinosaur BRACHIOSAURUS = new BrachiosaurusDinosaur();
    public static final Dinosaur COELACANTH = new CoelacanthDinosaur();
    public static final Dinosaur DILOPHOSAURUS = new DilophosaurusDinosaur();
    public static final Dinosaur GALLIMIMUS = new GallimimusDinosaur();
    public static final Dinosaur PARASAUROLOPHUS = new ParasaurolophusDinosaur();
    public static final Dinosaur MICRORAPTOR = new MicroraptorDinosaur();
    public static final Dinosaur MUSSAURUS = new MussaurusDinosaur();
    public static final Dinosaur TRICERATOPS = new TriceratopsDinosaur();
    public static final Dinosaur TYRANNOSAURUS = new TyrannosaurusDinosaur();
    public static final Dinosaur VELOCIRAPTOR = new VelociraptorDinosaur();
//    public static final Dinosaur ALLIGATORGAR = new AlligatorGarDinosaur();
    //public static final Dinosaur STEGeOSAURUS = new StegosaurusDinosaur();

    private static int entityId;


    public static void init() {
        registerEntity(AttractionSignEntity.class, "Attraction Sign");
        registerEntity(PaddockSignEntity.class, "Paddock Sign");
        registerEntity(MuralEntity.class, "Mural");
        registerEntity(VenomEntity.class, "Venom");

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
    
    @SubscribeEvent
    public static void onDinoRegistry(RegistryEvent.Register<Dinosaur> event) {
        event.getRegistry().registerAll(
                VELOCIRAPTOR.setRegistryName("velociraptor"),
                COELACANTH.setRegistryName("coelacanth"),
                MICRORAPTOR.setRegistryName("microraptor"),
                BRACHIOSAURUS.setRegistryName("brachiosaurus"),
                MUSSAURUS.setRegistryName("mussaurus"),
                DILOPHOSAURUS.setRegistryName("dilophosaurus"),
                GALLIMIMUS.setRegistryName("gallimimus"),
                PARASAUROLOPHUS.setRegistryName("parasaurolophus"),
                TRICERATOPS.setRegistryName("triceratops"),
                TYRANNOSAURUS.setRegistryName("tyrannosaurus")
        );
    }

}
