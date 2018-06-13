package org.jurassicraft.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.client.event.DinosaurModelHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.item.DinosaurProvider;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DinosaurItemModel extends BakedModelWrapper<IBakedModel> {

    private final Map<Dinosaur, Map<String, IBakedModel>> bakedModelMap;
    private IBakedModel model;

    public DinosaurItemModel(Map<Dinosaur, Map<String, IBakedModel>> bakedModelMap) {
        super(DinosaurModelHandler.MISSING_MODEL);
        this.bakedModelMap = bakedModelMap;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return model.getQuads(state, side, rand);
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(model, super.handlePerspective(cameraTransformType).getRight());
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Lists.newArrayList()) {
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                DinosaurProvider dinosaurProvider = DinosaurProvider.getFromStack(stack);
                model = bakedModelMap.getOrDefault(!dinosaurProvider.isMissing() ? dinosaurProvider.getDinosaur(stack) : null, Maps.newHashMap())
                        .getOrDefault(dinosaurProvider.getVarient(stack), DinosaurModelHandler.MISSING_MODEL);

                return super.handleItemState(originalModel, stack, world, entity);
            }
        };
    }
}
