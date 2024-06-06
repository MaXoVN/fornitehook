package me.fortnitehook.features.modules.combat;

import me.fortnitehook.OyVey;
import me.fortnitehook.event.events.UpdateWalkingPlayerEvent;
import me.fortnitehook.features.command.Command;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.util.DamageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ArmorMessage
        extends Module {
    private final Setting<Integer> armorThreshhold = this.register(new Setting<Integer>("Armor%", 20, 1, 100));
    private final Setting<Boolean> notifySelf = this.register(new Setting<Boolean>("NotifySelf", true));
    private final Map<EntityPlayer, Integer> entityArmorArraylist = new HashMap<EntityPlayer, Integer>();

    public ArmorMessage() {
        super("ArmorMessage", "Message friends when their armor is low", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEvent event) {
        for (EntityPlayer player : ArmorMessage.mc.world.playerEntities) {
            if (player.isDead || !OyVey.friendManager.isFriend(player.getName())) continue;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack == ItemStack.EMPTY) continue;
                int percent = DamageUtil.getRoundedDamage(stack);
                if (percent <= this.armorThreshhold.getValue() && !this.entityArmorArraylist.containsKey(player)) {
                    if (player == ArmorMessage.mc.player && this.notifySelf.getValue().booleanValue()) {
                        Command.sendMessage(player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!");
                    } else {
                        ArmorMessage.mc.player.sendChatMessage("/msg " + player.getName() + " " + player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!");
                    }
                    this.entityArmorArraylist.put(player, player.inventory.armorInventory.indexOf(stack));
                }
                if (!this.entityArmorArraylist.containsKey(player) || this.entityArmorArraylist.get(player).intValue() != player.inventory.armorInventory.indexOf(stack) || percent <= this.armorThreshhold.getValue())
                    continue;
                this.entityArmorArraylist.remove(player);
            }
            if (!this.entityArmorArraylist.containsKey(player) || player.inventory.armorInventory.get(this.entityArmorArraylist.get(player).intValue()) != ItemStack.EMPTY)
                continue;
            this.entityArmorArraylist.remove(player);
        }
    }

    private String getArmorPieceName(ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND_HELMET || stack.getItem() == Items.GOLDEN_HELMET || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET || stack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (stack.getItem() == Items.DIAMOND_LEGGINGS || stack.getItem() == Items.GOLDEN_LEGGINGS || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS || stack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}