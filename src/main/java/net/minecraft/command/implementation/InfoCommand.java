package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class InfoCommand extends AbstractCommand {
    public InfoCommand() {
        super("info", "information about the server", false);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        sender.addHelpCommandMessage("Server Information:");
        sender.addHelpCommandMessage("Players online: " + server.configManager.playerEntities.size());
        sender.addHelpCommandMessage("Level saving: " + (server.worldMngr.levelSaving ? "Disabled" : "Enabled"));
        sender.addHelpCommandMessage("Server running: " + server.serverRunning);
        sender.addHelpCommandMessage("https://github.com/toruuDev/MinecraftAlphaServer");
    }
}
