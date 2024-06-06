package me.fortnitehook.features.gui.components;

import me.fortnitehook.util.RenderUtil;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.Feature;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.gui.components.items.Item;
import me.fortnitehook.features.gui.components.items.buttons.Button;
import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.util.ColorUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Component
        extends Feature {
    public static int[] counter1 = new int[]{1};
    private final ArrayList<Item> items = new ArrayList();
    public boolean drag;
    private int x;
    private int y;

    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden = false;

    public Component(String string, int n, int n2, boolean bl) {
        super(string);
        this.x = n;
        this.y = n2;
        this.width = 100;
        this.height = 18;
        this.open = bl;
        this.setupItems();
    }

    public void setupItems() {
    }

    private void drag(int n, int n2) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + n;
        this.y = this.y2 + n2;
    }

    public void drawScreen(int n, int n2, float f) {
        this.drag(n, n2);
        counter1 = new int[]{1};
        float f2 = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        int n3 = ColorUtil.toARGB(ClickGui.getInstance().Color.getValue().getRed(), ClickGui.getInstance().Color.getValue().getGreen(), ClickGui.getInstance().Color.getValue().getBlue(), 255);
        Gui.drawRect(this.x, this.y - 1, this.x + this.width, this.y + this.height - 6, n3);
        if (this.open) {
            RenderUtil.drawRect(this.x, (float)this.y + 12.5f, this.x + this.width, (float)(this.y + this.height + 1) + f2, -2013265920);
        }
        OyVey.textManager.drawStringWithShadow(this.getName(), (float)this.x + 3.0f, (float)this.y - 4.0f - (float)OyVeyGui.getClickGui().getTextOffset(), -1);
        if (this.open) {
            float f3 = (float)(this.getY() + this.getHeight()) - 3.0f;
            for (Item item : this.getItems()) {
                Component.counter1[0] = counter1[0] + 1;
                if (item.isHidden()) continue;
                item.setLocation((float)this.x + 2.0f, f3);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(n, n2, f);
                f3 += (float)item.getHeight() + 1.5f;
            }
            if (this.open) {
                    if (ClickGui.INSTANCE().outline.getValue()) {
                        GlStateManager.disableTexture2D();
                        GlStateManager.enableBlend();
                        GlStateManager.disableAlpha();
                        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                        GlStateManager.shadeModel(7425);
                        GL11.glBegin(2);
                        GL11.glColor4f((float) ClickGui.INSTANCE().Color.getValue().getRed() / 255.0f, (float) ClickGui.INSTANCE().Color.getValue().getGreen() / 255.0f, (float) ClickGui.INSTANCE().Color.getValue().getBlue() / 255.0f, 255.0f);
                        GL11.glVertex3f((float) this.x, (float) this.y - 0.5f, 0.0f);
                        GL11.glVertex3f((float) (this.x + this.width), (float) this.y - 0.5f, 0.0f);
                        GL11.glVertex3f((float) (this.x + this.width), (float) (this.y + this.height) + f2, 0.0f);
                        GL11.glVertex3f((float) this.x, (float) (this.y + this.height) + f2, 0.0f);
                        GL11.glEnd();
                        GlStateManager.shadeModel(7424);
                        GlStateManager.disableBlend();
                        GlStateManager.enableAlpha();
                        GlStateManager.enableTexture2D();
                    }
            }
        }
    }

    public void mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.isHovering(n, n2)) {
            this.x2 = this.x - n;
            this.y2 = this.y - n2;
            OyVeyGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
            });
            this.drag = true;
            return;
        }
        if (n3 == 1 && this.isHovering(n, n2)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(n, n2, n3));
    }

    public void mouseReleased(int n, int n2, int n3) {
        if (n3 == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(n, n2, n3));
    }

    public void onKeyTyped(char c, int n) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(c, n));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int n) {
        this.x = n;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int n) {
        this.y = n;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean bl) {
        this.hidden = bl;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int n, int n2) {
        return n >= this.getX() && n <= this.getX() + this.getWidth() && n2 >= this.getY() && n2 <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float f = 0.0f;
        for (Item item : this.getItems()) {
            f += (float)item.getHeight() + 1.5f;
        }
        return f;
    }
}