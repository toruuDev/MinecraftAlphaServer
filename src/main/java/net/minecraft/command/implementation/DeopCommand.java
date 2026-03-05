package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class DeopCommand extends AbstractCommand {
    public DeopCommand() {
        super("de-op", "removes a player from being an op", true);
    }

    int targetName = 0;

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.configManager.deopPlayer(args[targetName]);
        server.print(args[targetName], "Deopping " + args[targetName]);
        server.configManager.sendChatMessageToPlayer(args[targetName], "\u00a7eYou are no longer op!");
    }
}

