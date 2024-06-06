package me.fortnitehook.features.gui.csgogui;

import java.awt.Color;
import java.util.ArrayList;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.client.CsgoGui;
import me.fortnitehook.util.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CategoryTerminal {
    public Module.Category category;
    public int x;
    public int y;
    public int width;
    public int height;
    public int deltaY2;
    float deltaY;
    float henk;
    public ArrayList<ModuleTerminal> modules = new ArrayList();
    public ArrayList<ModuleTerminal> finishedAnimationModules = new ArrayList();

    public CategoryTerminal(Module.Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.deltaY = y - height;
        this.deltaY2 = -18;
        int z = -18;
        this.henk = 0.0f;
        int finalZ = z += OyVey.moduleManager.getModulesByCategory(category).stream().mapToInt(module -> 22).sum();
        OyVey.moduleManager.getModulesByCategory(category).forEach(module -> this.modules.add(new ModuleTerminal(module, MainTerminal.x + 4, MainTerminal.afterCategoryY + (this.deltaY2 += 22) - finalZ, 130, 20)));
    }

    public void drawScreen(int n, int n2) {
        if (this.deltaY < (float)this.y) {
            this.deltaY += 1.0f;
        }
        if (this.isInsideModuleList(n, n2) && MainTerminal.currentCategory != null && MainTerminal.currentCategory.equals(this.category)) {
            this.scrollMouse();
        }
        RenderUtil.drawRect(this.x, this.deltaY, this.x + this.width, this.deltaY + (float)this.height, new Color(0, 0, 0, 50).getRGB());
        if (MainTerminal.currentCategory != null && MainTerminal.currentCategory.equals(this.category)) {
            if (this.henk < (float)this.width) {
                this.henk += 1.0f;
            }
        } else if (this.henk > 0.0f) {
            this.henk -= 1.0f;
        }
        if (MainTerminal.currentCategory != null && MainTerminal.currentCategory.equals(this.category) || this.henk > 0.0f) {
            RenderUtil.drawRect(this.x, this.deltaY, (float)this.x + this.henk, this.deltaY + (float)this.height, new Color(CsgoGui.Instance.red.getValue(), CsgoGui.Instance.green.getValue(), CsgoGui.Instance.blue.getValue(), CsgoGui.Instance.alpha.getValue()).getRGB());
        }
        RenderUtil.drawOutlineRect(this.x, this.deltaY, this.x + this.width, this.deltaY + (float)this.height, new Color(0, 0, 0, 100), 1.0f);
        OyVey.textManager.drawStringWithShadow(this.category.getName(), (float)this.x + (float)this.width / 2.0f - (float)OyVey.textManager.getStringWidth(this.category.getName()) / 2.0f, this.deltaY + (float)this.height / 2.0f - (float)OyVey.textManager.getFontHeight() / 2.0f, -1);
        int h = -18;
        for (ModuleTerminal moduleTerminal2 : this.modules) {
            if (this.finishedAnimationModules.contains(moduleTerminal2)) continue;
            if (moduleTerminal2.y < MainTerminal.afterCategoryY + (h += 22)) {
                moduleTerminal2.y += 2;
                continue;
            }
            this.finishedAnimationModules.add(moduleTerminal2);
        }
        if (this.isInside(n, n2)) {
            RenderUtil.drawRect(this.x, this.deltaY, this.x + this.width, this.deltaY + (float)this.height, new Color(0, 0, 0, 50).getRGB());
        }
        if (MainTerminal.currentCategory != null && MainTerminal.currentCategory.equals(this.category)) {
            GL11.glPushAttrib(524288);
            RenderUtil.scissor(MainTerminal.x + 3, this.y + this.height + 4, MainTerminal.x + 143, MainTerminal.totalHeight - 4);
            GL11.glEnable(3089);
            this.modules.forEach(moduleTerminal -> moduleTerminal.drawScreen(n, n2));
            GL11.glDisable(3089);
            GL11.glPopAttrib();
        }
    }

    public void mouseClicked(int n, int n2, int clickedButton) {
        if (this.isInside(n, n2) && clickedButton == 0) {
            int z = -18;
            int finalZ = z += OyVey.moduleManager.getModulesByCategory(this.category).stream().mapToInt(module -> 22).sum();
            if (MainTerminal.currentCategory != null && !MainTerminal.currentCategory.equals(this.category)) {
                int h = -18;
                for (ModuleTerminal moduleTerminal2 : this.modules) {
                    moduleTerminal2.y = MainTerminal.afterCategoryY + (h += 22) - finalZ;
                }
            }
            MainTerminal.currentCategory = this.category;
            this.finishedAnimationModules.clear();
        }
        if (MainTerminal.currentCategory != null && MainTerminal.currentCategory.equals(this.category)) {
            this.modules.stream().filter(moduleTerminal -> moduleTerminal.y > MainTerminal.afterCategoryY).forEach(moduleTerminal -> moduleTerminal.mouseClicked(n, n2, clickedButton));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (MainTerminal.currentCategory != null && MainTerminal.currentCategory.equals(this.category)) {
            GL11.glPushAttrib(524288);
            RenderUtil.scissor(MainTerminal.x + 3, this.y + this.height + 4, MainTerminal.x + 142, MainTerminal.totalHeight - 4);
            GL11.glEnable(3089);
            this.modules.forEach(moduleTerminal -> moduleTerminal.keyTyped(typedChar, keyCode));
            GL11.glDisable(3089);
            GL11.glPopAttrib();
        }
    }

    public void scrollMouse() {
        int dWheel = Mouse.getDWheel();
        this.modules.forEach(moduleTerminal -> {
            if (dWheel < 0) {
                moduleTerminal.y += 7;
            }
            if (dWheel > 0) {
                moduleTerminal.y -= 7;
            }
        });
    }

    public boolean isInsideModuleList(int n, int n2) {
        return n > MainTerminal.x + 2 && n < MainTerminal.x + 142 && n2 > MainTerminal.afterCategoryY + 4 && n2 < MainTerminal.totalHeight - 4;
    }

    public boolean isInside(int n, int n2) {
        return n > this.x && n < this.x + this.width && n2 > this.y && n2 < this.y + this.height;
    }
}
