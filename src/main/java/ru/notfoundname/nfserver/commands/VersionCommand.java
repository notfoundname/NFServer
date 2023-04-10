package ru.notfoundname.nfserver.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.Git;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.nfserver.NFServer;
import ru.notfoundname.nfserver.ServerProperties;

public class VersionCommand extends Command {

    public static final String PERMISSION = "nfserver.command.version";

    public VersionCommand() {
        super("version", "ver", "info");
        setCondition(((sender, commandString) ->
                sender instanceof ConsoleSender
                        || sender.hasPermission(PERMISSION)
                        || sender.hasPermission(ServerProperties.baseSettings.operatorPermission)));
        setDefaultExecutor(((sender, context) ->
                sender.sendMessage(Component.text("""
                        Server is running NFServer version %ver
                        Minecraft version: %min
                        Minestom commit: %stom"""
                        .replace("%ver", NFServer.VERSION)
                        .replace("%min", MinecraftServer.VERSION_NAME + " " + MinecraftServer.PROTOCOL_VERSION)
                        .replace("%stom", Git.commit())))));
    }
}
