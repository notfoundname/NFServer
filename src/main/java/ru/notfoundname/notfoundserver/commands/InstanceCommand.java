package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.notfoundserver.ServerProperties;

public class InstanceCommand extends Command {
    public static final String PERMISSION = "nfserver.command.instance";
    public InstanceCommand() {
        super("instance", "world");
        setCondition(((sender, commandString) ->
                sender instanceof ConsoleSender
                        || sender.hasPermission(PERMISSION)
                        || sender.hasPermission(ServerProperties.baseSettings.operatorPermission)));
        addSyntax(((sender, context) -> {

        }));
    }
}
