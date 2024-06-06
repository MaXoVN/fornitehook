package me.fortnitehook.util;

import me.fortnitehook.api.Minecraftable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

import java.util.Iterator;

public class ItemUtil
        implements Minecraftable {
    public static int getItemFromHotbar(Item item) {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = ItemUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() != item) continue;
            n = i;
        }
        return n;
    }

    public static int getItemSlot(Class clazz) {
        int n = -1;
        for (int i = 45; i > 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().getClass() != clazz) continue;
            n = i;
            break;
        }
        return n;
    }

    public static int getItemSlot(Item item) {
        int n = -1;
        for (int i = 45; i > 0; --i) {
            if (!ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().equals(item)) continue;
            n = i;
            break;
        }
        return n;
    }

    public static int getItemCount(Item item) {
        int n = 0;
        int n2 = ItemUtil.mc.player.inventory.mainInventory.size();
        for (int i = 0; i < n2; ++i) {
            ItemStack itemStack = ItemUtil.mc.player.inventory.mainInventory.get(i);
            if (itemStack.getItem() != item) continue;
            ++n;
        }
        if (ItemUtil.mc.player.getHeldItemOffhand().getItem() == item) {
            ++n;
        }
        return n;
    }

    public static boolean isArmorLow(EntityPlayer entityPlayer, int n) {
        Iterator iterator = entityPlayer.inventory.armorInventory.iterator();
        while (iterator.hasNext()) {
            ItemStack itemStack = (ItemStack)iterator.next();
            if (itemStack != null && ItemUtil.getItemDamage(itemStack) >= n) continue;
            return true;
        }
        return false;
    }

    public static int getItemDamage(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }

    public static float getDamageInPercent(ItemStack itemStack) {
        return (float)ItemUtil.getItemDamage(itemStack) / (float)itemStack.getMaxDamage() * 100.0f;
    }

    public static int getRoundedDamage(ItemStack itemStack) {
        return (int)ItemUtil.getDamageInPercent(itemStack);
    }

    public static boolean hasDurability(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }
}