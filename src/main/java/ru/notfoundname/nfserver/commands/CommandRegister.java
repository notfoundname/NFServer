package ru.notfoundname.nfserver.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import ru.notfoundname.nfserver.ServerProperties;

public class CommandRegister {

    public static void register(CommandManager commandManager) {
        commandManager.register(new ExtensionsCommand());
        commandManager.register(new ReloadCommand());
        commandManager.register(new RestartCommand());
        commandManager.register(new StopCommand());
        commandManager.register(new VersionCommand());

        commandManager.setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(ServerProperties.translations.unknownCommand));
            if (sender instanceof Player) {
                MinecraftServer.LOGGER.info(((Player) sender).getName() + " tried to use command " + command);
            }
        });
    }
}
