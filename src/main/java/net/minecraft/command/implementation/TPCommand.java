package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.player.EntityPlayerMP;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class TPCommand extends AbstractCommand {

    public TPCommand() {
        super("tp", "teleports a player to another player", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {

        String sourceName = args[0];
        String targetName = args[1];

        EntityPlayerMP source = server.configManager.getPlayerEntity(sourceName);
        EntityPlayerMP target = server.configManager.getPlayerEntity(targetName);

        if (source == null) {
            sender.addHelpCommandMessage("Can't find user " + sourceName + ". No tp.");
            return;
        }

        if (target == null) {
            sender.addHelpCommandMessage("Can't find user " + targetName + ". No tp.");
            return;
        }

        source.playerNetServerHandler.teleportTo(
                target.posX, target.posY, target.posZ,
                target.rotationYaw,
                target.rotationPitch
        );

        server.print(
                sender.getUsername(),
                "Teleporting " + sourceName + " to " + targetName + "."
        );
    }
}