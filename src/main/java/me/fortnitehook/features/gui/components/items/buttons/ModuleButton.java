
package me.fortnitehook.features.gui.components.items.buttons;

import me.fortnitehook.OyVey;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.gui.components.Component;
import me.fortnitehook.features.gui.components.items.Item;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.features.setting.Bind;
import me.fortnitehook.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton
        extends Button {
    private final Module module;
    private int logs;

    private final ResourceLocation logo = new ResourceLocation("textures/oyvey.png");
    private List<Item> items = new ArrayList<Item>();
    private boolean subOpen;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }





    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if (setting.getValue() instanceof String || setting.getValue() instanceof Character) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.getValue() instanceof Color)
                    newItems.add(new ColorButton(setting));

                if (setting.isNumberSetting()) {
                    if (setting.hasRestriction()) {
                        newItems.add(new Slider(setting));
                        continue;
                    }
                    newItems.add(new UnlimitedSlider(setting));
                }
                if (!setting.isEnumSetting()) continue;
                newItems.add(new EnumButton(setting));
            }
        }
        this.items = newItems;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            if (subOpen) {
                float height = 1.0f;
                for (Item item : items) {
                    if (!item.isHidden()) {
                        if (item instanceof ColorButton) {
                            item.setLocation(x + 1.0f, y + (height + 15.0f));
                        } else {
                            item.setLocation(x + 1.0f, y + (height += 15.0f));
                        }
                        item.setHeight((int) 15.0f);
                        item.setWidth(width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                        if (item instanceof ColorButton)
                            height += item.getHeight();
                        if (item instanceof EnumButton && ((EnumButton) item).setting.isOpen)
                            height += ((EnumButton) item).setting.getValue().getClass().getEnumConstants().length * 12;
                    }
                    item.update();
                }

                OyVey.textManager.drawString("-",this.x + this.width - 9, this.y + 4.0f, 0xFFFFFF, true);
                float f2 = 1.0f;
                for (Item item : this.items) {
                    Component.counter1[0] = Component.counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (f2 += 15.0f));
                        item.setHeight(15);
                        item.setWidth(this.width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                    item.update();
                }
            }
            else
            {
                OyVey.textManager.drawString("+",this.x + this.width - 9, this.y + 4.0f, 0xFFFFFF, true);
            }
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (!this.items.isEmpty()) {
            if (n3 == 1 && this.isHovering(n, n2)) {
                this.subOpen = !this.subOpen;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(n, n2, n3);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char c, int n) {
        super.onKeyTyped(c, n);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(c, n);
            }
        }
    }


    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}