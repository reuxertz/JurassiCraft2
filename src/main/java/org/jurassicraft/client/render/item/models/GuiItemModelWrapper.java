package org.jurassicraft.client.render.item.models;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class GuiItemModelWrapper implements IPerspectiveAwareModel{

    private final IBakedModel originalModel;
    private final IBakedModel guiModel;
    
    public GuiItemModelWrapper(IBakedModel originalModel, IBakedModel guiModel) {
	this.originalModel = originalModel;
	this.guiModel = guiModel;
    }
    
    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
	return originalModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
	return originalModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
	return originalModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
	return originalModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
	return originalModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
	return originalModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
	return originalModel.getOverrides();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
	return Pair.of(cameraTransformType == TransformType.GUI ? guiModel : originalModel, new TRSRTransformation(getItemCameraTransforms().getTransform(cameraTransformType)).getMatrix());
    }
    
    

}
