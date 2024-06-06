package me.fortnitehook.util;


import net.minecraft.client.Minecraft;

public class NullUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean fullNullCheck() {
        return mc.player == null || mc.world == null;
    }
}