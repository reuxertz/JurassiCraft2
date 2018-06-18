package org.jurassicraft.server.item;

import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.tab.TabHandler;

public class DinoScannerItem extends Item {
    public DinoScannerItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }
}
