package ru.notfoundname.notfoundserver.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.Git;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import ru.notfoundname.notfoundserver.NFServer;

public class VersionCommand extends Command {
    public static final String PERMISSION = "nfserver.command.version";
    public VersionCommand() {
        super("version", "ver", "info");
        setDefaultExecutor(((sender, context) ->
                sender.sendMessage(Component.text("""
                        Server is running NFServer version %ver
                        Minecraft version: %min
                        Minestom commit: %stom
                        """
                        .replace("%ver", NFServer.VERSION)
                        .replace("%min", MinecraftServer.VERSION_NAME + " " + MinecraftServer.PROTOCOL_VERSION)
                        .replace("%stom", Git.commit())))));
    }

}
