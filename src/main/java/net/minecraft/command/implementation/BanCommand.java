package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.player.EntityPlayerMP;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class BanCommand extends AbstractCommand {
    public BanCommand() {
        super("ban", "bans a player from the server", true);
    }

    int targetName = 0;

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {
        EntityPlayerMP playerMp;

        server.configManager.banPlayer(args[targetName]);
        server.print(sender.getUsername(), "Banning " + args[targetName]);

        playerMp = server.configManager.getPlayerEntity(args[targetName]);
        if(playerMp != null) {
            playerMp.playerNetServerHandler.kickPlayer("Banned by admin");
        }
    }
}
