package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class PardonCommand extends AbstractCommand {
    public PardonCommand() {
        super("pardon", "pardons a player from the server", true);
    }

    int targetName = 0;

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.configManager.pardonPlayer(args[targetName]);
        server.print(sender.getUsername(), "Pardoning " + args[targetName]);
    }
}
