package me.fortnitehook.features.modules.client;


import me.fortnitehook.Discord;
import me.fortnitehook.features.modules.Module;

public class RPC extends Module {

    public static RPC INSTANCE;

    public RPC() {
        super("Discord", "Discord rich presence", Module.Category.CLIENT, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Discord.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stop();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Discord.start();
    }
}