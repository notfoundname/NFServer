package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import ru.notfoundname.notfoundserver.NFServer;
import ru.notfoundname.notfoundserver.ServerProperties;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "end", "shutdown", "quit", "");
        setCondition(((sender, commandString) -> {
            if (sender instanceof Player) {
                return ServerProperties.config.allowAllPlayersToExecuteDefaultCommands;
            } else return true;
        }));
        setDefaultExecutor((sender, context) -> {
            NFServer.stop();
        });
    }
}
