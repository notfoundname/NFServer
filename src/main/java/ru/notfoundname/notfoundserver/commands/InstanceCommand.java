package ru.notfoundname.notfoundserver.commands;

import net.minestom.server.command.builder.Command;

public class InstanceCommand extends Command {
    public static final String PERMISSION = "nfserver.command.instance";
    public InstanceCommand() {
        super("instance", "world");
        addSyntax(((sender, context) -> {

        }));
    }
}
