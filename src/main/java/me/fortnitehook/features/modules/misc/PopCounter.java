
package me.fortnitehook.features.modules.misc;

import java.util.*;

import me.fortnitehook.features.command.Command;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import net.minecraft.entity.player.*;
import com.mojang.realmsclient.gui.*;

public class PopCounter extends Module
{
    public static HashMap<String, Integer> TotemPopContainer;
    private static PopCounter INSTANCE;
    private final Setting<Boolean> showOwn;

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Category.MISC, true, false, true);
        this.showOwn = (Setting<Boolean>)this.register(new Setting("Show Own", true));
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (PopCounter.INSTANCE == null) {
            PopCounter.INSTANCE = new PopCounter();
        }
        return PopCounter.INSTANCE;
    }

    private void setInstance() {
        PopCounter.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        PopCounter.TotemPopContainer.clear();
    }

    public void onDeath(final EntityPlayer player) {
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            final int l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.remove(player.getName());
            if (this.isOn()) {
                Command.sendTempMessageID( ChatFormatting.RESET + player.getName() + " died after popping their " + ChatFormatting.GREEN + l_Count + this.getPopString(l_Count) + " totem!", -42069);
            }
        }
    }

    public void onTotemPop(final EntityPlayer player) {
        if (fullNullCheck()) {
            return;
        }
        if (PopCounter.mc.player.equals(player) && !this.showOwn.getValue()) {
            return;
        }
        int l_Count = 1;
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.put(player.getName(), ++l_Count);
        }
        else {
            PopCounter.TotemPopContainer.put(player.getName(), l_Count);
        }
        if (this.isOn()) {
            Command.sendTempMessageID(ChatFormatting.RESET + player.getName() + " popped their " + ChatFormatting.GREEN + l_Count + this.getPopString(l_Count) + ChatFormatting.WHITE + " totem.", -1337);
        }
    }

    public String getPopString(final int pops) {
        if (pops == 1) {
            return "st";
        }
        if (pops == 2) {
            return "nd";
        }
        if (pops == 3) {
            return "rd";
        }
        if (pops >= 4 && pops < 21) {
            return "th";
        }
        final int lastDigit = pops % 10;
        if (lastDigit == 1) {
            return "st";
        }
        if (lastDigit == 2) {
            return "nd";
        }
        if (lastDigit == 3) {
            return "rd";
        }
        return "th";
    }






    static {
        PopCounter.TotemPopContainer = new HashMap<String, Integer>();
        PopCounter.INSTANCE = new PopCounter();
    }
}
