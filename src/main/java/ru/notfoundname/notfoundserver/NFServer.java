package ru.notfoundname.notfoundserver;

import io.github.bloepiloepi.pvp.PvpExtension;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.world.Difficulty;
import ru.notfoundname.notfoundserver.commands.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class NFServer {
    // Automatically replaced by gradle
    public static String VERSION = "nfsversion";

    public static void main(String[] args) {
        try {
            ServerProperties.reload();
            System.setProperty("minestom.chunk-view-distance", String.valueOf(ServerProperties.gameSettings.viewDistance));
            System.setProperty("minestom.entity-view-distance", String.valueOf(ServerProperties.gameSettings.viewSimulationDistance));
            if (!ServerProperties.baseSettings.terminalEnabled) {
                System.setProperty("minestom.terminal.disabled", "");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize server configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }

        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.LOGGER.info("Initializing NFServer " + VERSION);

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        if (!Files.exists(Path.of("instances/" + ServerProperties.gameSettings.levelName))) {
            try {
                Files.createDirectory(Path.of("instances/" + ServerProperties.gameSettings.levelName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(
                new AnvilLoader("instances/" + ServerProperties.gameSettings.levelName));

        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK));

        try {
            MinecraftServer.setDifficulty(Difficulty.valueOf(ServerProperties.gameSettings.difficulty.toUpperCase()));
        } catch (Exception e) {
            MinecraftServer.LOGGER.warn("Unknown difficulty " + ServerProperties.gameSettings.difficulty);
            MinecraftServer.setDifficulty(Difficulty.PEACEFUL);
        }

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerPreLoginEvent.class, event -> {
           if (ServerProperties.baseSettings.whiteList) {
                event.getPlayer().kick(
                        MiniMessage.miniMessage().deserialize(ServerProperties.translations.serverWhitelisted));
           }
        });
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            try {
                player.setGameMode(GameMode.valueOf(ServerProperties.gameSettings.gamemode.toUpperCase()));
            } catch (IllegalArgumentException e) {
                MinecraftServer.LOGGER.warn("Unknown gamemode " + ServerProperties.gameSettings.gamemode);
                player.setGameMode(GameMode.ADVENTURE);
            }
            player.setRespawnPoint(new Pos(0, 2, 0));
        });
        globalEventHandler.addListener(PlayerChatEvent.class, event ->
                MinecraftServer.LOGGER.info(event.getPlayer().getName() + " - " + event.getMessage()));
        globalEventHandler.addListener(ServerListPingEvent.class, event -> {
            event.getResponseData().setDescription(
                    MiniMessage.miniMessage().deserialize(ServerProperties.baseSettings.motd));
            event.getResponseData().setMaxPlayer(ServerProperties.baseSettings.maxPlayers);
            event.getResponseData().setPlayersHidden(ServerProperties.baseSettings.hideOnlinePlayers);
        });

        MinecraftServer.getCommandManager().register(new StopCommand());
        MinecraftServer.getCommandManager().register(new VersionCommand());
        MinecraftServer.getCommandManager().register(new ExtensionsCommand());
        MinecraftServer.getCommandManager().register(new RestartCommand());
        MinecraftServer.getCommandManager().register(new ReloadCommand());
        MinecraftServer.getCommandManager().register(new InstanceCommand());

        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(ServerProperties.translations.unknownCommand));
            if (sender instanceof Player) {
                MinecraftServer.LOGGER.info(((Player) sender).getDisplayName() + " tried to use command " + command);
            }
        });

        if (ServerProperties.baseSettings.broadcastToLan) {
            OpenToLAN.open();
        }

        if (ServerProperties.gameSettings.pvpExtensionEnabled) {
            PvpExtension.init();
        }

        MinecraftServer.setTerminalEnabled(ServerProperties.baseSettings.terminalEnabled);

        switch (ServerProperties.baseSettings.connectionMode.toUpperCase(Locale.ROOT)) {
            case "OFFLINE":
                break;
            case "ONLINE":
                MojangAuth.init();
                break;
            case "BUNGEECORD":
                BungeeCordProxy.enable();
                break;
            case "VELOCITY":
                VelocityProxy.enable(ServerProperties.baseSettings.connectionModeSecret);
                break;
            default:
                MinecraftServer.LOGGER.warn("Unknown connection mode: " + ServerProperties.baseSettings.connectionMode);
                break;
        }

        minecraftServer.start(ServerProperties.baseSettings.serverIp, ServerProperties.baseSettings.serverPort);
        MinecraftServer.LOGGER.info("Server started at " +
                ServerProperties.baseSettings.serverIp + ":" +
                ServerProperties.baseSettings.serverPort);
    }

    public static void stop() {
        MinecraftServer.LOGGER.info("Shutting down NFServer " + VERSION);
        MinecraftServer.LOGGER.info("Saving levels");
        MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
        System.exit(0);
    }

    public static void restart() throws IOException {
        stop();
        Runtime.getRuntime().exec(ServerProperties.baseSettings.restartScript);
    }
}