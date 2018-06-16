package org.jurassicraft.client.event;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurItemModel;
import org.jurassicraft.server.api.StackNBTProvider;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class DinosaurModelHandler {

    private static Map<Item, DinosaurItemModel> itemMap = Maps.newHashMap();

    public static IBakedModel MISSING_MODEL;

    @SubscribeEvent
    public static void onModelsBaked(ModelBakeEvent event) {
        MISSING_MODEL = event.getModelManager().getMissingModel();
        IRegistry<ModelResourceLocation, IBakedModel> map = event.getModelRegistry();
        itemMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().shouldRegister())
                .forEach(entry -> map.putObject(new ModelResourceLocation(entry.getKey().getRegistryName(), "inventory"), entry.getValue()));
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onTextureStitched(TextureStitchEvent event) {
        for(Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
            StackNBTProvider provider = StackNBTProvider.getFromStack(new ItemStack(item));
            if(provider == null) {
                continue;
            }
            itemMap.put(item, new DinosaurItemModel(provider, event.getMap()));
        }
    }


    public static class NonIndentedPrintStream extends PrintStream {

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
