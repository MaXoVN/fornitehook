package me.fortnitehook.features.modules.misc;

import me.fortnitehook.features.modules.*;
import me.fortnitehook.event.events.PacketEvent;
import me.fortnitehook.features.command.Command;
import me.fortnitehook.features.modules.Module;
import net.minecraft.entity.player.*;
import me.fortnitehook.event.events.*;
import net.minecraft.network.play.server.*;
import me.fortnitehook.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;

public class PotionDetect extends Module
{
    private Set<EntityPlayer> str;

    public PotionDetect() {
        super("PotionDetect", "Detects the potions someone has", Category.MISC, true, false, false);
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }

    @Override
    public void onToggle() {
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityEffect) {
            final SPacketEntityEffect sPacketEntityEffect = event.getPacket();
            Command.sendMessage("Potion ID: " + sPacketEntityEffect.getEntityId());
        }
    }

    @Override
    public void onUpdate() {
        for (final EntityPlayer player : PotionDetect.mc.world.playerEntities) {
            if (player.equals(PotionDetect.mc.player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                Command.sendMessage("§d" + player.getName() + ChatFormatting.AQUA + " has strength");
                this.str.add(player);
            }
            if (!this.str.contains(player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH)) {
                continue;
            }
            Command.sendMessage("§d" + player.getName() + ChatFormatting.AQUA + " no longer has strength!");
            this.str.remove(player);
        }
    }
}