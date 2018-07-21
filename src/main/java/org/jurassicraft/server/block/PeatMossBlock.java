package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.api.IncubatorEnvironmentItem;

class PeatMossBlock extends Block implements IncubatorEnvironmentItem {
     PeatMossBlock() {
        super(Material.GROUND);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5F);
    }
}
