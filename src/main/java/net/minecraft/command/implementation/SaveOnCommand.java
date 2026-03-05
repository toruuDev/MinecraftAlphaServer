package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class SaveOnCommand extends AbstractCommand {
    public SaveOnCommand() {
        super("save-on", "enable level saving", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.print(sender.getUsername(), "enabling level saving...");
        server.worldMngr.levelSaving = true;
    }
}