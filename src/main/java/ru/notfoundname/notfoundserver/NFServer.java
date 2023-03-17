package ru.notfoundname.notfoundserver;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.SimpleCommand;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Locale;

public class NFServer {

    public static String VERSION = "nfsversion";

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.LOGGER.info("Initializing NFServer " + VERSION);

        try {
            ServerProperties.initialize();
        } catch (IOException e) {
            System.err.println("Failed to initialize server configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK));

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 2, 0));
        });

        MinecraftServer.getCommandManager().register(new SimpleCommand("stop") {
            @Override
            public boolean process(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
                stop();
                return true;
            }
            @Override
            public boolean hasAccess(@NotNull CommandSender sender, @Nullable String commandString) {
                return true;
            }
        });

        switch (ServerProperties.config.connectionMode.toUpperCase(Locale.ROOT)) {
            case "OFFLINE":
                break;
            case "ONLINE":
                MojangAuth.init();
                break;
            case "BUNGEECORD":
                BungeeCordProxy.enable();
                break;
            case "VELOCITY":
                VelocityProxy.enable(ServerProperties.config.connectionModeSecret);
                break;
            default:
                MinecraftServer.LOGGER.warn("Unknown connection mode " + ServerProperties.config.connectionMode);
                break;
        }

        minecraftServer.start(ServerProperties.config.serverIp, ServerProperties.config.serverPort);
        MinecraftServer.LOGGER.info("Server started at " + ServerProperties.config.serverIp + ":" + ServerProperties.config.serverPort);
    }

    public static void stop() {
        System.exit(0);
    }
}