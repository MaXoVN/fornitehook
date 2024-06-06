package me.fortnitehook.features.modules.combat;

import me.fortnitehook.features.modules.*;
import me.fortnitehook.features.setting.*;
import me.fortnitehook.event.events.Render2DEvent;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import net.minecraft.item.*;
import me.fortnitehook.event.events.*;
import net.minecraft.client.gui.*;

public class DurabilityAlert extends Module
{
    private final Setting<Integer> dura;
    private final Setting<Boolean> chad;
    private boolean lowDura;

    public DurabilityAlert() {
        super("DurabilityAlert", "Alerts you and your friends if you have low durability", Category.COMBAT, true, false, false);
        this.dura = (Setting<Integer>)this.register(new Setting("Durability", 10, 1, 100));
        this.chad = (Setting<Boolean>)this.register(new Setting("British Mode", true));
        this.lowDura = false;
    }

    @Override
    public void onUpdate() {
        this.lowDura = false;
        try {
            for (final ItemStack is : DurabilityAlert.mc.player.getArmorInventoryList()) {
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                if (dmg > (float)this.dura.getValue()) {
                    continue;
                }
                this.lowDura = true;
            }
        }
        catch (Exception ex) {}
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (this.lowDura) {
            final ScaledResolution sr = new ScaledResolution(DurabilityAlert.mc);
            DurabilityAlert.mc.fontRenderer.drawStringWithShadow("Warning: Your " + (this.chad.getValue() ? "armour" : "shits") + " is below " + this.dura.getValue() + "%", (float)(sr.getScaledWidth() / 2 - DurabilityAlert.mc.fontRenderer.getStringWidth("Warning: Your armour is below " + this.dura.getValue() + "%") / 2), 15.0f, -65536);
        }
    }
}