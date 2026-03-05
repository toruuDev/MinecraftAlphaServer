package net.minecraft.command.implementation;

import net.minecraft.command.AbstractCommand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.player.EntityPlayerMP;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class GiveCommand extends AbstractCommand {

    public GiveCommand() {
        super("give", "gives a player an item", true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandListener sender, String[] args) {

        if (args.length != 2 && args.length != 3) {
            sender.addHelpCommandMessage("Usage: give <player> <itemId> [amount]");
            return;
        }

        String playerName = args[0];
        EntityPlayerMP player = server.configManager.getPlayerEntity(playerName);

        if (player == null) {
            sender.addHelpCommandMessage("Can't find user " + playerName);
            return;
        }

        try {

            int itemId = Integer.parseInt(args[1]);

            if (Item.itemsList[itemId] == null) {
                sender.addHelpCommandMessage("There's no item with id " + itemId);
                return;
            }

            int amount = 1;

            if (args.length == 3) {
                amount = server.parseInt(args[2], 1);
            }

            if (amount < 1) amount = 1;
            if (amount > 64) amount = 64;

            server.print(
                    sender.getUsername(),
                    "Giving " + player.username + " some " + itemId
            );

            player.dropPlayerItem(new ItemStack(itemId, amount));

        } catch (NumberFormatException e) {
            sender.addHelpCommandMessage("There's no item with id " + args[1]);
        }
    }
}