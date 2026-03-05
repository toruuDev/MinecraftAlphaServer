package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class SaveOffCommand extends AbstractCommand {
    public SaveOffCommand() {
        super("save-off", "disables level saving", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.print(sender.getUsername(), "disabling level saving...");
        server.worldMngr.levelSaving = false;
    }
}
