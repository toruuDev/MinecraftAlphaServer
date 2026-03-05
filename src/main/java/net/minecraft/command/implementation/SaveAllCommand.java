package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;

public class SaveAllCommand extends AbstractCommand {
    public SaveAllCommand() {
        super("save-all", "forces a serverwide level save", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.print(sender.getUsername(), "forcing save...");
        server.worldMngr.saveWorld(true, (IProgressUpdate) null);
        server.print(sender.getUsername(), "save complete...");
    }
}