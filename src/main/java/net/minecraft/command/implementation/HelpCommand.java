package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "shows this", false);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {

        sender.addHelpCommandMessage("Available commands:");

        for(AbstractCommand cmd : server.commandManager.commands.values()) {

            if(cmd.requiresOp && !server.configManager.isOp(sender.getUsername())) {
                continue;
            }

            sender.addHelpCommandMessage(cmd.name + " - " + cmd.description);
        }
    }
}