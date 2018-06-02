package org.jurassicraft.server.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindingHandler {
    public static KeyBinding MICRORAPTOR_DISMOUNT = new KeyBinding("key.microraptor_dismount", Keyboard.KEY_C, "JurassiCraft");
    public static KeyBinding VEHICLE_SWITCH_SEAT_CONTROL = new KeyBinding("key.vehicle.switch", new IKeyConflictContext() {
        @Override
        public boolean isActive() {
            EntityPlayer player = Minecraft.getMinecraft().player;
            return player != null && player.getRidingEntity() instanceof CarEntity;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return false;
        }
    }, Keyboard.KEY_LCONTROL, "JurassiCraft");

    public static void init() {
        ClientRegistry.registerKeyBinding(MICRORAPTOR_DISMOUNT);
        ClientRegistry.registerKeyBinding(VEHICLE_SWITCH_SEAT_CONTROL);

    }
}
