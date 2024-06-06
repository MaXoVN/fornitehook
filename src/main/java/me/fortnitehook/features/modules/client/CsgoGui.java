package me.fortnitehook.features.modules.client;

import me.fortnitehook.features.gui.csgogui.MainTerminal;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;

public class CsgoGui
extends Module {
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public static CsgoGui Instance;

    public CsgoGui() {
        super("CsgoGui", "Displays csgo gui", Module.Category.CLIENT, false, false, false);
        Instance = this;
        this.setBind(54);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new MainTerminal());
    }
}
