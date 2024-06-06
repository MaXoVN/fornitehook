package me.fortnitehook.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public interface Minecraftable {
    Minecraft mc = Minecraft.getMinecraft();

    Minecraft mc2 = Minecraft.getMinecraft();
    static Minecraft GetMC()
    {
        return mc2;
    }

    static EntityPlayerSP GetPlayer()
    {
        return mc2.player;
    }
}