package me.fortnitehook.features.command.commands;

import me.fortnitehook.OyVey;
import me.fortnitehook.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        OyVey.reload();
    }
}

