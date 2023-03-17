package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import ru.notfoundname.notfoundserver.ServerProperties;

public class RestartCommand extends Command {
    public RestartCommand() {
        super("restart", "reload");
        setCondition(((sender, commandString) -> {
            if (sender instanceof Player) {
                return ServerProperties.config.allowAllPlayersToExecuteDefaultCommands;
            } else return true;
        }));
    }
}
