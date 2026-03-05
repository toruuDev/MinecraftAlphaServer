package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class SayCommand extends AbstractCommand {

    public SayCommand() {
        super("say", "broadcasts a message to all players", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {

        String message = String.join(" ", args);

        MinecraftServer.logger.info("[" + sender.getUsername() + "] " + message);

        server.configManager.sendPacketToAllPlayers(
                new Packet3Chat("\u00a7d[Server] " + message)
        );
    }
}
