package me.fortnitehook.features.command.commands;

import me.fortnitehook.OyVey;
import me.fortnitehook.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        OyVey.unload(true);
    }
}

