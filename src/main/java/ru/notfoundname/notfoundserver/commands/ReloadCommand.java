package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.notfoundserver.ServerProperties;

import java.io.IOException;

public class ReloadCommand extends Command {
    public static final String PERMISSION = "nfserver.command.reload";
    public ReloadCommand() {
        super("reload");
        setCondition(((sender, commandString) ->
                sender instanceof ConsoleSender
                        || sender.hasPermission(PERMISSION)
                        || sender.hasPermission(ServerProperties.baseSettings.operatorPermission)));
        addSyntax((sender, context) -> {
            try {
                ServerProperties.reload();
                sender.sendMessage("Probably reloaded");
            } catch (IOException e) {
                sender.sendMessage("Something happened");
                e.printStackTrace();
            }
        });
    }
}
