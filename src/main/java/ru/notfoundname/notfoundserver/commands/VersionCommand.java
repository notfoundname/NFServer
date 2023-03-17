package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.builder.Command;
import ru.notfoundname.notfoundserver.NFServer;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("version");
        setDefaultExecutor(((sender, context) ->
                sender.sendMessage("This server is running NFServer version " + NFServer.VERSION)));
    }

}
