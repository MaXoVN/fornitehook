package me.fortnitehook.features.command;



import me.fortnitehook.util.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.Feature;
import me.fortnitehook.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command
        extends Feature {
    protected String name;
    protected String[] commands;

    public Command(String name) {
        super(name);
        this.name = name;
        this.commands = new String[]{""};
    }

    public Command(String name, String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }

    public static void sendMessage(String message) {
        Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + message);
    }

    public static void sendMsg(String message) {
        Command.sendSilentMessage(message);
    }

    public static void sendSilentMessage(String message) {
        if (Command.nullCheck()) {
            return;
        }
        Command.mc.player.sendMessage(new ChatMessage(message));
    }


    public static void sendRemovableMessage(String message, int id) {
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(message), id);
    }
    public static void sendTempMessageID(final String message, final int id) {
        sendTempSilentMessage(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.GRAY + message, id);
    }
    public static void sendTempSilentMessage(final String message, final int id) {
        if (nullCheck()) {
            return;
        }
        Util.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatMessage(message), id);
    }
    public static String getCommandPrefix() {
        return OyVey.commandManager.getPrefix();
    }

    public abstract void execute(String[] var1);

    @Override
    public String getName() {
        return this.name;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public static class ChatMessage
            extends TextComponentBase {
        private final String text;

        public ChatMessage(String text) {
            Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher matcher = pattern.matcher(text);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                String replacement = matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return null;
        }

        public ITextComponent shallowCopy() {
            return new ChatMessage(this.text);
        }
    }
}

