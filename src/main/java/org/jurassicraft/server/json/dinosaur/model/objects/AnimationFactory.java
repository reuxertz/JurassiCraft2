package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.gson.JsonArray;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;

@FunctionalInterface
public interface AnimationFactory {

    AnimationCallable createCallback(JsonArray array, JsonDinosaurAnimator animator);

}
