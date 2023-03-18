package ru.notfoundname.notfoundserver.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import ru.notfoundname.notfoundserver.ServerProperties;

import java.io.Console;
import java.util.Arrays;

public class ExtensionsCommand extends Command {

    public ExtensionsCommand() {
        super("extensions", "exts");
        setCondition(((sender, commandString) -> {
            if (sender instanceof Player) {
                return ServerProperties.config.allowAllPlayersToExecuteDefaultCommands;
            } else return true;
        }));
        addSyntax(((sender, context) -> {
            if (MinecraftServer.getExtensionManager().getExtensions().isEmpty()) {
                sender.sendMessage("There are no enabled extensions.");
            } else {
                sender.sendMessage("Extensions:");
                if (sender instanceof ConsoleSender) {
                    for (Extension extension : MinecraftServer.getExtensionManager().getExtensions()) {
                        sender.sendMessage(Component.text()
                                .content("- Name: ")
                                .append(Component.text(extension.getOrigin().getName()))
                                .append(Component.text(("\n  Version: ")))
                                .append(Component.text(extension.getOrigin().getVersion()))
                                .append(Component.text("\n  Authors: "))
                                .append(Component.text(Arrays.toString(extension.getOrigin().getAuthors())))
                                .build());
                    }
                } else {
                    Component component = Component.text("");
                    for (Extension extension : MinecraftServer.getExtensionManager().getExtensions()) {
                        component = component
                                .append(Component.text(extension.getOrigin().getName())
                                .hoverEvent(HoverEvent
                                        .showText(Component.text(
                                                "Version: " + extension.getOrigin().getVersion()
                                                + "\nAuthors: " + Arrays.toString(extension.getOrigin().getAuthors()
                                                )))))
                                .append(Component.text(" "));
                    }
                    sender.sendMessage(component);
                }
            }
        }));
    }
}
