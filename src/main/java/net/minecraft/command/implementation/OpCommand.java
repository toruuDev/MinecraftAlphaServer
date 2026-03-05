package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class OpCommand extends AbstractCommand {
    public OpCommand() {
        super("op", "turns a player into an op", true);
    }

    int targetName = 0;

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.configManager.opPlayer(args[targetName]);
        server.print(args[targetName], "Opping " + args[targetName]);
        server.configManager.sendChatMessageToPlayer(args[targetName], "\u00a7eYou are now op!");
    }
}
