
package me.fortnitehook.features.gui.components.items.buttons;

import me.fortnitehook.util.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.features.setting.Bind;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.util.ColorUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BindButton
        extends Button {
    private final Setting setting;
    public boolean isListening;

    public BindButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        int n3 = ColorUtil.toARGB(ClickGui.getInstance().Color.getValue().getRed(), ClickGui.getInstance().Color.getValue().getGreen(), ClickGui.getInstance().Color.getValue().getBlue(), 255);
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(n, n2) ? 0x11555555 : -2007673515) : (!this.isHovering(n, n2) ? OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).Color.getValue().getAlpha()) : OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).HoverAlpha.getValue())));
        if (this.isListening) {
            OyVey.textManager.drawStringWithShadow("Press Sumthin...", this.x + 2.3f, this.y - 1.7f - (float)OyVeyGui.getClickGui().getTextOffset(), -1);
        } else {
            OyVey.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 1.7f - (float)OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isHovering(n, n2)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char c, int n) {
        if (this.isListening) {
            Bind bind = new Bind(n);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            this.onMouseClick();
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }
}
 