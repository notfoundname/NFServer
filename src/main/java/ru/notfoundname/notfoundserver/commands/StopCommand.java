package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.notfoundserver.NFServer;
import ru.notfoundname.notfoundserver.ServerProperties;

public class StopCommand extends Command {
    public static final String PERMISSION = "nfserver.command.stop";
    public StopCommand() {
        super("stop", "end", "shutdown", "quit");
        setCondition(((sender, commandString) ->
                sender instanceof ConsoleSender
                        || sender.hasPermission(PERMISSION)
                        || sender.hasPermission(ServerProperties.baseSettings.operatorPermission)));
        setDefaultExecutor((sender, context) -> NFServer.stop());
    }
}
