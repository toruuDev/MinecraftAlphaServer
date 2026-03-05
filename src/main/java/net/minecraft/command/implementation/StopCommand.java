package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop", "stops the server", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.print(sender.getUsername(), "stopping the server...");
        server.serverRunning = false;
    }
}
