package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class ListCommand extends AbstractCommand {
    public ListCommand() {
        super("list", "lists all the currently active players", false);
    }


    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        sender.addHelpCommandMessage("Connected players: " + server.configManager.getPlayerList());
    }
}
