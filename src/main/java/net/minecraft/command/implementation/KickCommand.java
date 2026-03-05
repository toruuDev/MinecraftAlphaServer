package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.player.EntityPlayerMP;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class KickCommand extends AbstractCommand {
    public KickCommand() {
        super("kick", "kicks a player from the level", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        EntityPlayerMP playerMp;

        String targetName = args[0];

        EntityPlayerMP player = server.configManager.getPlayerEntity(targetName);

        if (player == null) {
            sender.addHelpCommandMessage("Can't find user " + targetName);
            return;
        }

        player.playerNetServerHandler.kickPlayer("Kicked by admin");

        server.print(sender.getUsername(), "Kicking " + player.username);
    }
}
