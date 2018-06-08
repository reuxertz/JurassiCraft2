package org.jurassicraft.server.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PaleoBaleBlock extends BlockRotatedPillar {
    private PaleoBaleBlock.Variant variant;

    public PaleoBaleBlock(PaleoBaleBlock.Variant variant) {
        super(Material.GRASS, MapColor.FOLIAGE);
        this.variant = variant;
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
        this.setCreativeTab(TabHandler.PLANTS);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        entity.fall(fallDistance, 0.2F);
    }

    public PaleoBaleBlock.Variant getVariant() {
        return this.variant;
    }

    public enum Variant implements IStringSerializable {
        CYCADEOIDEA(BlockHandler.CYCADEOIDEA),
        CYCAD(BlockHandler.SMALL_CYCAD),
        FERN(BlockHandler.SMALL_CHAIN_FERN, BlockHandler.SMALL_ROYAL_FERN, BlockHandler.RAPHAELIA, BlockHandler.BRISTLE_FERN, BlockHandler.CINNAMON_FERN, BlockHandler.TEMPSKYA),
        LEAVES(BlockHandler.ANCIENT_LEAVES.values().toArray(new Block[BlockHandler.ANCIENT_LEAVES.size()])),
        OTHER(BlockHandler.AJUGINUCULA_SMITHII, BlockHandler.RHACOPHYTON, BlockHandler.GRAMINIDITES_BAMBUSOIDES, BlockHandler.HELICONIA, BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT, BlockHandler.LARGESTIPULE_LEATHER_ROOT, BlockHandler.CRY_PANSY, BlockHandler.DICKSONIA, BlockHandler.DICROIDIUM_ZUBERI, BlockHandler.WILD_ONION, BlockHandler.ZAMITES, BlockHandler.UMALTOLEPIS, BlockHandler.LIRIODENDRITES, BlockHandler.WOOLLY_STALKED_BEGONIA);

        private List<ItemStack> itemStackList = Lists.newArrayList();
        private List<Block> blockList = Lists.newArrayList();

        Variant(Block... ingredients) {
            blockList.addAll(Lists.newArrayList(ingredients));
        }

        @SuppressWarnings("unused")
        Variant(Item... ingredients) {
            itemStackList.addAll(Lists.newArrayList(ingredients).stream().map(ItemStack::new).collect(Collectors.toList()));
        }

        @Override
        public String getName() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public List<ItemStack> getIngredients() {
            List<ItemStack> list =  Lists.newArrayList(itemStackList);
            list.addAll(blockList.stream().map(ItemStack::new).collect(Collectors.toList()));
            return list;
        }
    }
}
