package org.jurassicraft.server.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;
import org.jurassicraft.server.tab.TabHandler;

public class DinoScannerItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public DinoScannerItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof DinosaurEntity && !player.getEntityWorld().isRemote) {
            DinosaurEntity dinosaur = (DinosaurEntity) target;
            if (player.isSneaking()) {
                int food = dinosaur.getMetabolism().getEnergy();
                dinosaur.getMetabolism().setEnergy(food - 5000);
                LOGGER.info("food: " + dinosaur.getMetabolism().getEnergy() + "/" + dinosaur.getMetabolism().getMaxEnergy() +
                        "(" + (dinosaur.getMetabolism().getMaxEnergy() * 0.875) + "/" +
                        (dinosaur.getMetabolism().getMaxEnergy() * 0.25) + ")");
            } else {
                dinosaur.writeStatsToLog();
            }

            return true;
        }
        return false;
    }
}
