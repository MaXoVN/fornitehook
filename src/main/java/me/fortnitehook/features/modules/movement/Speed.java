package me.fortnitehook.features.modules.movement;

import me.fortnitehook.features.modules.Module;

public class Speed
        extends Module {
    public Speed() {
        super("Speed", "Speed.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public String getDisplayInfo() {
        return "Strafe";
    }
}

