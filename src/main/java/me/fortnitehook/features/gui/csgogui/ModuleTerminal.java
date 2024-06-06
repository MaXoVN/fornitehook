package me.fortnitehook.features.gui.csgogui;

import java.awt.Color;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.client.CsgoGui;
import me.fortnitehook.util.RenderUtil;

public class ModuleTerminal {
    public Module module;
    public int x;
    public int y;
    public int width;
    public int height;
    public float henk;

    public ModuleTerminal(Module module, int x, int y, int width, int height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.henk = module.isEnabled() ? (float)width : 0.0f;
    }

    public void drawScreen(int n, int n2) {
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(0, 0, 0, 40).getRGB());
        if (this.henk > 0.0f) {
            RenderUtil.drawRect(this.x, this.y, (float)this.x + this.henk, this.y + this.height, new Color(CsgoGui.Instance.red.getValue(), CsgoGui.Instance.green.getValue(), CsgoGui.Instance.blue.getValue(), CsgoGui.Instance.alpha.getValue()).getRGB());
        }
        if (this.module.isEnabled()) {
            if (this.henk < (float)this.width) {
                this.henk += 2.0f;
            }
        } else if (this.henk > 0.0f) {
            this.henk -= 2.0f;
        }
        RenderUtil.drawOutlineRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(0, 0, 0, 100), 1.0f);
        OyVey.textManager.drawStringWithShadow(this.module.getName(), this.x + 2, (float)this.y + (float)this.height / 2.0f - (float)OyVey.textManager.getFontHeight() / 2.0f, -1);
        if (this.isInside(n, n2)) {
            RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(0, 0, 0, 50).getRGB());
        }
    }

    public void mouseClicked(int n, int n2, int clickedButton) {
        if (this.isInside(n, n2) && clickedButton == 0) {
            if (this.module.isEnabled()) {
                this.module.disable();
            } else {
                this.module.enable();
            }
        }
        if (this.isInside(n, n2) && clickedButton == 1) {
            MainTerminal.currentModule = this.module;
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public boolean isInside(int n, int n2) {
        return n > this.x && n < this.x + this.width && n2 > this.y && n2 < this.y + this.height;
    }
}
