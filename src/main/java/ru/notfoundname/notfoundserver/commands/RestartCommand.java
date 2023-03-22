package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.notfoundserver.NFServer;
import ru.notfoundname.notfoundserver.ServerProperties;

import java.io.IOException;

public class RestartCommand extends Command {
    public static final String PERMISSION = "nfserver.command.restart";
    public RestartCommand() {
        super("restart", "reload", "reboot");
        setCondition(((sender, commandString) -> {
            if (sender instanceof ConsoleSender)
                return true;
            else return sender.hasPermission(ServerProperties.baseSettings.operatorPermission);
        }));
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
