package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;

import java.util.List;

/**
 * A single animation module. The Json Parser will search in the inner map of {@link JsonAnimationRegistry#factoryMap}
 * for the animation module registered to this type.
 *
 * <p>In the following json, there are three modules, called "bob", "chain_swing" and "facing".
 * "bob" and "chain_swing" are both under the type "limb_swing", with "facing" being under the type "look".
 * All of the modules need extra information. For example, "bob" needs a list of all cubes to apply to, and
 * values for "speed" and "degree". This is what V is used for, to store extra information.
 * Also not that in the following json, in module "bob", {@link #createValue(JsonObject, JsonAnimator)} will be called twice,
 * as there are two objects. (First starting with the "constant", second starting with the "names")
 * </p>
 * <pre>
 * {
 *     "global_speed": 1.0,
 *     "global_degree": 1.0,
 *     "constants": [
 *         {
 *           "key": "tail_parts",
 *           "names": ["tail6", "tail5", "tail4", "tail3", "tail2", "tail1"]
 *         }
 *     ],
 *     "animations": [{
 *             "type": "limb_swing",
 *             "bob": [
 *                 {"constant": "tail_parts", "speed": 0.5, "degree": 1.0},
 *                 {"names": ["neck", "sholder1", "sholder1", "sholder3"], "speed": 0.5, "degree": 1.0}
 *             ],
 *             "chain_swing": [
 *                 { "names": ["tail1", "tail2", "tail3", "tail4"], "speed": 0.1, "degree": 1, "root_offset": 1}
 *             ]
 *         },
 *         {
 *             "type": "look",
 *             "facing": [
 *                 { "names": ["neck1", "head"], "divisor": 1.0}
 *             ]
 *         }
 *     ]
 * }
 * </pre>
 * @param <V> The class used to get and store information about this callable. See above for more detail
 * @author Wyn Price
 */
public abstract class JsonAnimationModule<V> {

    protected final List<V> list = Lists.newArrayList();
    protected final JsonAnimator animator;

    protected JsonAnimationModule(JsonArray array, JsonAnimator animator) {
        this.animator = animator;
        String name = this.getClass().toString();
        for (JsonElement jsonElement : array) {
            list.add(this.createValue(JsonUtils.getJsonObject(jsonElement, name), animator));
        }
    }

    public void performAnimations(TabulaModel model, Entity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (V value : this.list) {
            this.performAnimation(model, entity, value, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
        }
    }

    protected abstract void performAnimation(TabulaModel model, Entity entity, V value, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale);

    public abstract V createValue(JsonObject json, JsonAnimator animator);
}