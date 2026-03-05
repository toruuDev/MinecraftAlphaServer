package net.minecraft.command;

/*
Added by toru
NOT originally in the minecraft server source code
*/

import net.minecraft.command.implementation.*;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public Map<String, AbstractCommand> commands = new HashMap<>();

    public void register(AbstractCommand command) {
        commands.put(command.name.toLowerCase(), command);
    }

    public CommandManager() {
        register(new BanCommand());
        register(new BanIPCommand());
        register(new DeopCommand());
        register(new GiveCommand());
        register(new InfoCommand());
        register(new KickCommand());
        register(new ListCommand());
        register(new OpCommand());
        register(new PardonCommand());
        register(new PardonIPCommand());
        register(new SaveAllCommand());
        register(new SaveOffCommand());
        register(new SaveOnCommand());
        register(new SayCommand());
        register(new StopCommand());
        register(new TellCommand());
        register(new TPCommand());
        register(new HelpCommand());
    }

    public void executeCommand(MinecraftServer server, ICommandListener sender, String input) {
        String[] split = input.split(" ");
        String name = split[0].toLowerCase();

        AbstractCommand command = commands.get(name);

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        if (command == null) {
            sender.addHelpCommandMessage("Unknown command. use /help to get a list of commands.");
            return;
        }

        if(command.requiresOp && !server.configManager.isOp(sender.getUsername())) {
            sender.addHelpCommandMessage("You must have elevated privileges to send this command.");
            return;
        }

        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);

        command.execute(server, sender, args);
    }
}
