package org.jurassicraft.server.datafixers;

import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jurassicraft.JurassiCraft;

public class JurassiCraftDataFixers {
    private static final int DATAFIXER_VERSION = 1;
    private static final ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(JurassiCraft.MODID, DATAFIXER_VERSION);

    public static void init() {
        modFixs.registerFix(FixTypes.ENTITY, new DataFixerFactory(1, compound -> {
            if("jurassicraft.mural".equals(compound.getString("id"))) {
                compound.setString("id", "jurassicraft:entities.mural");
            }
            return compound;
        }));
    }
}