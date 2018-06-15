package org.jurassicraft.client.event;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurItemModel;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.item.DinosaurProvider;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class DinosaurModelHandler {

    private static final Map<Item, Map<Dinosaur, Map<String, IBakedModel>>> MODEL_MAP = Maps.newHashMap();

    public static IBakedModel MISSING_MODEL;

    @SubscribeEvent
    public static void onModelsBaked(ModelBakeEvent event) {
        MISSING_MODEL = event.getModelManager().getMissingModel();
        IRegistry<ModelResourceLocation, IBakedModel> map = event.getModelRegistry();
        MODEL_MAP.forEach((item, dinosaurIBakedModelMap) -> map.putObject(new ModelResourceLocation(item.getRegistryName(), "inventory"), new DinosaurItemModel(dinosaurIBakedModelMap)));
    }

    @SubscribeEvent
    public static void onTextureStitched(TextureStitchEvent event) {
        for(Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
            DinosaurProvider dinosaurProvider = DinosaurProvider.getFromStack(new ItemStack(item));
            if(dinosaurProvider.isMissing()) {
                continue;
            }
           Map<Dinosaur, Map<String, IBakedModel>> modelMap = Maps.newHashMap();
            JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection().stream().filter(dinosaurProvider::shouldOverrideModel).forEach(dinosaur ->
                dinosaurProvider.getModelResourceLocations(dinosaur).forEach((s, location) -> {
                    try {
                        modelMap.computeIfAbsent(dinosaur, dino -> Maps.newHashMap()).put(s, getModel(location, event.getMap()));
                    } catch (Exception e) {
                        e.printStackTrace(NonIndentedPrintStream.INSTANCE);
                    }
                })
            );
            if(!modelMap.isEmpty()) {
                MODEL_MAP.put(item, modelMap);
            }
        }
    }

    private static IBakedModel getModel(ResourceLocation resourceLocation, TextureMap map) throws Exception {
        return ModelLoaderRegistry.getModel(resourceLocation).bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, map::registerSprite);
    }

    private static class NonIndentedPrintStream extends PrintStream {

        public static final NonIndentedPrintStream INSTANCE = new NonIndentedPrintStream();

        private NonIndentedPrintStream() {
            super(new NonIndentedOutputStream());
        }
    }
    private static class NonIndentedOutputStream extends OutputStream {

        @Override
        public void write(int b) {
            System.out.print((char)b);
        }

    }
}
