package me.fortnitehook.features.modules.render;

import me.fortnitehook.features.modules.*;
import me.fortnitehook.features.setting.*;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;

public class CrystalScale extends Module
{
    public Setting<Float> scale;
    public static CrystalScale INSTANCE;

    public CrystalScale() {
        super("CrystalScale", "", Module.Category.RENDER, true, false, false);
        this.scale = (Setting<Float>)this.register(new Setting("Scale", 6.25f, 0.01f, 10.0f));
        CrystalScale.INSTANCE = this;
    }

    public void onEnable() {
        CrystalScale.INSTANCE = this;
    }

    public static CrystalScale getInstance() {
        if (CrystalScale.INSTANCE == null) {
            CrystalScale.INSTANCE = new CrystalScale();
        }
        return CrystalScale.INSTANCE;
    }

    public float getScale() {
        return this.scale.getValue() / 10.0f;
    }
}