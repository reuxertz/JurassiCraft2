package org.jurassicraft.server.entity.vehicle.modules;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public abstract class HelicopterModule {
    public static final Map<String, Class<? extends HelicopterModule>> registry;

    static {
        // register default modules
        registry = Maps.newHashMap();
        registry.put("door", HelicopterDoor.class);
        registry.put("minigun", HelicopterMinigun.class);
    }

    private final String moduleID;
    private final Collection<Class<? extends HelicopterModule>> supported;

    protected HelicopterModule(String id) {
        this.moduleID = id;
        this.supported = this.createSupportedModuleList();
    }

    public static HelicopterModule createFromID(String id) {
        Class<? extends HelicopterModule> clazz = registry.get(id);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected abstract Collection<Class<? extends HelicopterModule>> createSupportedModuleList();

    public Collection<Class<? extends HelicopterModule>> getSupportedModules() {
        return this.supported;
    }

    public abstract float getBaseRotationAngle();

    public String getModuleID() {
        return this.moduleID;
    }

    public abstract boolean onClicked(HelicopterModuleSpot m, EntityPlayer player, Vec3d vec);

    public void onAdded(HelicopterModuleSpot m, EntityPlayer player, Vec3d vec) {

    }

    public void onRemoved(HelicopterModuleSpot m, EntityPlayer player, Vec3d vec) {

    }

    public abstract void writeToNBT(NBTTagCompound compound);

    public abstract void readFromNBT(NBTTagCompound compound);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HelicopterModule) {
            return this.getModuleID().equals(((HelicopterModule) obj).getModuleID());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getModuleID().hashCode();
    }
}
