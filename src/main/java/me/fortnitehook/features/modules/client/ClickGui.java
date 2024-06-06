package me.fortnitehook.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.OyVey;
import me.fortnitehook.event.events.ClientEvent;
import me.fortnitehook.features.command.Command;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.util.TextUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "?"));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = this.register(new Setting<Float>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", false));
    public Setting<Boolean> darkBackGround = this.register(new Setting<Object>("DarkBackgound", true));
    public Setting<Integer> HoverAlpha = this.register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, object -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, object -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), object -> this.rainbow.getValue()));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), object -> this.rainbow.getValue()));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), object -> this.rainbow.getValue()));
    public Setting<Color> Color = this.register(new Setting<Color>("Color", new Color(140, 35, 255, 100)));
    private OyVeyGui click;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    public static ClickGui INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent clientEvent) {
        if (clientEvent.getStage() == 2 && clientEvent.getSetting().getFeature().equals(this)) {
            if (clientEvent.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }
            OyVey.colorManager.setColor(this.Color.getPlannedValue().getRed(), this.Color.getPlannedValue().getGreen(), this.Color.getPlannedValue().getBlue(), this.Color.getPlannedValue().getAlpha());
        }
    }

    public String getCommandMessage() {
        return TextUtil.coloredString("[", HUD.INSTANCE().bracketColor.getPlannedValue()) + TextUtil.coloredString("fortnitehook", HUD.INSTANCE().commandColor.getPlannedValue()) + TextUtil.coloredString("]", HUD.INSTANCE().bracketColor.getPlannedValue());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(OyVeyGui.getClickGui());
    }

    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha());
        OyVey.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            this.disable();
        }
    }

    public enum rainbowMode {
        Static,
        Sideway

    }

    public enum rainbowModeArray {
        Static,
        Up

    }
}