package me.fortnitehook;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.fortnitehook.features.modules.client.RPC;


public class Discord {

    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static RPC discordrpc;
    private static Thread thread;

    public static void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("1093522624316977262", handlers, true, "");
        Discord.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        Discord.presence.details = "i love fortnite";
        Discord.presence.state = "yumyumyum";
        Discord.presence.largeImageKey = "amongusdripshotty";
        Discord.presence.largeImageText = OyVey.MODVER;
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                Discord.presence.details = "i love fortnite";
                Discord.presence.state = "yumyumyum";
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    static {
        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
    }
}