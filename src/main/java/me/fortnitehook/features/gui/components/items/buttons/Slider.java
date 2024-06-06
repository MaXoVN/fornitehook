
package me.fortnitehook.features.gui.components.items.buttons;

import me.fortnitehook.util.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.gui.components.Component;
import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.features.setting.Setting;
import org.lwjgl.input.Mouse;

public class Slider
        extends Button {
    private final Number min;
    private final Number max;
    private final int difference;
    public Setting setting;

    public Slider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number)setting.getMin();
        this.max = (Number)setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.dragSetting(n, n2);
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(n, n2) ? 0x11555555 : -2007673515);
        RenderUtil.drawRect(this.x, this.y, ((Number)this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float)this.width + 7.4f) * this.partialMultiplier(), this.y + (float)this.height - 0.5f, !this.isHovering(n, n2) ? OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).Color.getValue().getAlpha()) : OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).HoverAlpha.getValue()));
        OyVey.textManager.drawStringWithShadow(this.getName() + " " + ChatFormatting.GRAY + (this.setting.getValue() instanceof Float ? this.setting.getValue() : Double.valueOf(((Number)this.setting.getValue()).doubleValue())), this.x + 2.3f, this.y - 1.7f - (float)OyVeyGui.getClickGui().getTextOffset(), -1);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isHovering(n, n2)) {
            this.setSettingFromX(n);
        }
    }

    @Override
    public boolean isHovering(int n, int n2) {
        for (Component component : OyVeyGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float)n >= this.getX() && (float)n <= this.getX() + (float)this.getWidth() + 8.0f && (float)n2 >= this.getY() && (float)n2 <= this.getY() + (float)this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int n, int n2) {
        if (this.isHovering(n, n2) && Mouse.isButtonDown(0)) {
            this.setSettingFromX(n);
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    private void setSettingFromX(int n) {
        float f = ((float)n - this.x) / ((float)this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double d = (Double)this.setting.getMin() + (double)((float)this.difference * f);
            this.setting.setValue((double)Math.round(10.0 * d) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float f2 = ((Float)this.setting.getMin()).floatValue() + (float)this.difference * f;
            this.setting.setValue(Float.valueOf((float)Math.round(10.0f * f2) / 10.0f));
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer)this.setting.getMin() + (int)((float)this.difference * f));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}
 