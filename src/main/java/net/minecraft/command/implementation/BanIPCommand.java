package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class BanIPCommand extends AbstractCommand {
    public BanIPCommand() {
        super("ban-ip", "bans the ip of a player", true);
    }

    int targetName = 0;

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        server.configManager.banIP(args[targetName]);
        server.print(sender.getUsername(), "Banning ip " + args[targetName]);
    }
}
