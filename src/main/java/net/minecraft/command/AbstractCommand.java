package net.minecraft.command;

/*
Added by toru
NOT originally in minecraft server source code
*/

import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractCommand {
    public final String name;
    public final String description;
    public final boolean requiresOp;

    public AbstractCommand(String name, String description, boolean requiresOp) {
        this.name = name;
        this.description = description;
        this.requiresOp = requiresOp;
    }

    public abstract void execute(MinecraftServer server, ICommandListener sender, String[] args);
}
