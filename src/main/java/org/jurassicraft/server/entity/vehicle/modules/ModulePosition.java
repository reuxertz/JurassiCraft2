package org.jurassicraft.server.entity.vehicle.modules;

import com.google.common.base.Function;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public enum ModulePosition {
    FRONT(vec -> vec.z > 0.6),
    LEFT_SIDE(vec -> vec.z < 0.6 && vec.x > 0),
    RIGHT_SIDE(vec -> vec.z < 0.6 && vec.x < 0);

    private final Function<Vec3d, Boolean> func;

    ModulePosition(Function<Vec3d, Boolean> clickCheckFunc) {
        this.func = Objects.requireNonNull(clickCheckFunc);
    }

    /**
     * Checks if the module is been clicked.
     *
     * @param v The helicopter-relative raytrace produced by the player trying to interact
     * @return True if the raytrace ends up in this module, false otherwise
     */
    public boolean isClicked(Vec3d v) {
        return this.func.apply(v);
    }
}
