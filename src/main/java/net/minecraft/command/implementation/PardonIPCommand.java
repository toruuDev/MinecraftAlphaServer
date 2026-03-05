package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class PardonIPCommand extends AbstractCommand {
    public PardonIPCommand() {
        super("pardon", "Unbans a banned player", true);
    }

    int targetName = 0;

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.configManager.pardonIP(args[targetName]);
        server.print(sender.getUsername(), "Pardoning ip " + args[targetName]);
    }
}
