package me.fortnitehook.features.gui.csgogui;

import java.awt.Color;
import java.util.ArrayList;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.client.CsgoGui;
import me.fortnitehook.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public class MainTerminal
extends GuiScreen {
    public ArrayList<CategoryTerminal> categoryTerminalArrayList = new ArrayList();
    public int deltaX = 0;
    public static Module.Category currentCategory = Module.Category.CLIENT;
    public static Module currentModule = null;
    public static int x = 60;
    public static int y = 25;
    public static int afterBarY = 40;
    public static int afterCategoryY = 57;
    public static int afterModuleWidth = x + 134;
    public static int totalHeight = 400;

    public MainTerminal() {
        OyVey.moduleManager.getCategories().forEach(category -> this.categoryTerminalArrayList.add(new CategoryTerminal((Module.Category) category, this.deltaX += 62, 42, 60, 15)));
    }

    public void drawScreen(int n, int n2, float partialTicks) {
        RenderUtil.drawRect(x, y, this.deltaX + 64, afterBarY, new Color(CsgoGui.Instance.red.getValue(), CsgoGui.Instance.green.getValue(), CsgoGui.Instance.blue.getValue(), CsgoGui.Instance.alpha.getValue()).getRGB());
        OyVey.textManager.drawStringWithShadow("fortnitehook " + OyVey.MODVER, x + 2, 32.5f - (float)OyVey.textManager.getFontHeight() / 2.0f, -1);
        RenderUtil.drawRect(x, afterBarY, this.deltaX + 64, totalHeight, new Color(0x363636).getRGB());
        RenderUtil.drawOutlineRect(x, y, this.deltaX + 64, totalHeight, new Color(CsgoGui.Instance.red.getValue(), CsgoGui.Instance.green.getValue(), CsgoGui.Instance.blue.getValue(), CsgoGui.Instance.alpha.getValue()), 2.0f);
        RenderUtil.drawRect(x + 2, afterCategoryY + 2, this.deltaX + 60, totalHeight - 2, new Color(0x262626).getRGB());
        RenderUtil.drawOutlineRect(x + 2, afterCategoryY + 2, this.deltaX + 60, totalHeight - 2, new Color(0x1A1A1A), 2.0f);
        RenderUtil.drawRect(afterModuleWidth + 2, afterCategoryY + 4, this.deltaX + 58, totalHeight - 4, new Color(0x2D2D2D).getRGB());
        RenderUtil.drawOutlineRect(afterModuleWidth + 2, afterCategoryY + 4, this.deltaX + 58, totalHeight - 4, new Color(0x1A1A1A), 2.0f);
        GL11.glPushAttrib(524288);
        RenderUtil.scissor(x, afterBarY, this.deltaX + 62, totalHeight - 2);
        GL11.glEnable(3089);
        this.categoryTerminalArrayList.forEach(categoryTerminal -> categoryTerminal.drawScreen(n, n2));
        GL11.glDisable(3089);
        GL11.glPopAttrib();
    }

    public void mouseClicked(int n, int n2, int clickedButton) {
        this.categoryTerminalArrayList.forEach(categoryTerminal -> categoryTerminal.mouseClicked(n, n2, clickedButton));
    }

    public void keyTyped(char typedChar, int keyCode) {
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.categoryTerminalArrayList.forEach(categoryTerminal -> categoryTerminal.keyTyped(typedChar, keyCode));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
