//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Elijah\Downloads\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 */
package me.fortnitehook.features.modules.client;

import me.fortnitehook.OyVey;
import me.fortnitehook.event.events.Render2DEvent;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.util.ColorUtil;
import me.fortnitehook.util.RenderUtil;
import me.fortnitehook.util.Timer;

public class Csgo
extends Module {
    Timer delayTimer = new Timer();
    public Setting<Integer> X = this.register(new Setting<Integer>("watermarkx", 0, 0, 300));
    public Setting<Integer> Y = this.register(new Setting<Integer>("watermarky", 0, 0, 300));
    public Setting<Integer> delay = this.register(new Setting<Integer>("delay", 240, 0, 600));
    public Setting<Integer> saturation = this.register(new Setting<Integer>("saturation", 127, 1, 255));
    public Setting<Integer> brightness = this.register(new Setting<Integer>("brightness", 100, 0, 255));
    public float hue;
    public int red = 1;
    public int green = 1;
    public int blue = 1;
    private String message = "";

    public Csgo() {
        super("csgo", "Nice Screen Extras", Module.Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.drawCsgoWatermark();
    }

    public void drawCsgoWatermark() {
        int padding = 5;
        this.message = "FortniteHook " + OyVey.MODVER + " " +OyVey.MODBUILD + " : "  + Csgo.mc.player.getName() + " : " + OyVey.serverManager.getPing() + "Ms";
        Integer textWidth = Csgo.mc.fontRenderer.getStringWidth(this.message);
        Integer textHeight = Csgo.mc.fontRenderer.FONT_HEIGHT;
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 150));
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, 2, ColorUtil.toRGBA(ClickGui.INSTANCE().Color.getValue().getRed(), ClickGui.INSTANCE().Color.getValue().getGreen(), ClickGui.INSTANCE().Color.getValue().getBlue()));
        Csgo.mc.fontRenderer.drawString(this.message, (float)(this.X.getValue() + 3), (float)(this.Y.getValue() + 3), ColorUtil.toRGBA(255, 255, 255, 255), false);
    }
}

