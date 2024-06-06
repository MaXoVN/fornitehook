
package me.fortnitehook.features.gui.components.items.buttons;

import me.fortnitehook.util.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.features.setting.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class UnlimitedSlider
        extends Button {
    public Setting setting;

    public UnlimitedSlider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(n, n2) ? OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).Color.getValue().getAlpha()) : OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).HoverAlpha.getValue()));
        OyVey.textManager.drawStringWithShadow(" - " + this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue() + ChatFormatting.WHITE + " +", this.x + 2.3f, this.y - 1.7f - (float)OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isHovering(n, n2)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            if (this.isRight(n)) {
                if (this.setting.getValue() instanceof Double) {
                    this.setting.setValue((Double)this.setting.getValue() + 1.0);
                } else if (this.setting.getValue() instanceof Float) {
                    this.setting.setValue(Float.valueOf(((Float)this.setting.getValue()).floatValue() + 1.0f));
                } else if (this.setting.getValue() instanceof Integer) {
                    this.setting.setValue((Integer)this.setting.getValue() + 1);
                }
            } else if (this.setting.getValue() instanceof Double) {
                this.setting.setValue((Double)this.setting.getValue() - 1.0);
            } else if (this.setting.getValue() instanceof Float) {
                this.setting.setValue(Float.valueOf(((Float)this.setting.getValue()).floatValue() - 1.0f));
            } else if (this.setting.getValue() instanceof Integer) {
                this.setting.setValue((Integer)this.setting.getValue() - 1);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean getState() {
        return true;
    }

    public boolean isRight(int n) {
        return (float)n > this.x + ((float)this.width + 7.4f) / 2.0f;
    }
}
 