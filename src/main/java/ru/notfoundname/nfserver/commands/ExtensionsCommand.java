package ru.notfoundname.nfserver.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.extensions.Extension;
import ru.notfoundname.nfserver.ServerProperties;

import java.util.Arrays;

public class ExtensionsCommand extends Command {

    public static final String PERMISSION = "nfserver.command.extensions";
    public ExtensionsCommand() {
        super("extensions", "exts");
        setCondition(((sender, commandString) ->
                sender instanceof ConsoleSender
                        || sender.hasPermission(PERMISSION)
                        || sender.hasPermission(ServerProperties.baseSettings.operatorPermission)));
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
