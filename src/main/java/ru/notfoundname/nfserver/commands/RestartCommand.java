package ru.notfoundname.nfserver.commands;

import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.nfserver.NFServer;
import ru.notfoundname.nfserver.ServerProperties;

import java.io.IOException;

public class RestartCommand extends Command {
    public static final String PERMISSION = "nfserver.command.restart";
    public RestartCommand() {
        super("restart", "reboot");
        setCondition(((sender, commandString) ->
                sender instanceof ConsoleSender
                        || sender.hasPermission(PERMISSION)
                        || sender.hasPermission(ServerProperties.baseSettings.operatorPermission)));
        addSyntax((sender, context) -> {
            try {
                NFServer.restart();
            } catch (IOException e) {
                System.out.println("Cannot restart, just stopping then.");
                NFServer.stop();
            }
        });
    }
}
