package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class TellCommand extends AbstractCommand {

    public TellCommand() {
        super("tell", "sends a private message to a player", false);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {

        if (args.length < 2) {
            sender.addHelpCommandMessage("Usage: tell <player> <message>");
            return;
        }

        String targetName = args[0];

        // rebuild message
        StringBuilder msgBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msgBuilder.append(args[i]).append(" ");
        }

        String message = msgBuilder.toString().trim();

        MinecraftServer.logger.info(
                "[" + sender.getUsername() + "->" + targetName + "] " + message
        );

        String whisper = "\u00a77" + sender.getUsername() + " whispers " + message;

        MinecraftServer.logger.info(whisper);

        if (!server.configManager.sendPacketToPlayer(
                targetName,
                new Packet3Chat(whisper)
        )) {
            sender.addHelpCommandMessage("There's no player by that name online.");
        }
    }
}
