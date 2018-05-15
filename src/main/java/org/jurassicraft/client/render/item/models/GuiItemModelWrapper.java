package org.jurassicraft.client.render.item.models;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.client.model.BakedModelWrapper;

public class GuiItemModelWrapper extends BakedModelWrapper<IBakedModel> {

    private final IBakedModel guiModel;
    
    public GuiItemModelWrapper(IBakedModel originalModel, IBakedModel guiModel) {
	super(originalModel);
	this.guiModel = guiModel;
    }
    
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return cameraTransformType == TransformType.GUI ? guiModel.handlePerspective(cameraTransformType) : super.handlePerspective(cameraTransformType);
    }

}
