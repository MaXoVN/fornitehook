package me.fortnitehook.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.OyVey;
import me.fortnitehook.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Usable commands: ");
        for (Command command : OyVey.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.WHITE + OyVey.commandManager.getPrefix() + command.getName());
        }
    }
}

