package ru.notfoundname.nfserver;

import io.github.togar2.fluids.MinestomFluids;
import me.waterdev.minestombasiclight.LightEngine;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.extras.velocity.VelocityProxy;
import ru.notfoundname.nfserver.commands.*;
import ru.notfoundname.nfserver.events.EventRegister;
import ru.notfoundname.nfserver.instances.InstanceRegister;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NFServer {
    /* Automatically replaced by gradle */
    public static String VERSION = "nfsversion";

    public static LightEngine lightEngine;

    public static void main(String[] args) {
        try {
            ServerProperties.reload();
            System.setProperty("minestom.chunk-view-distance",
                    String.valueOf(ServerProperties.gameSettings.viewDistance));
            System.setProperty("minestom.entity-view-distance",
                    String.valueOf(ServerProperties.gameSettings.viewSimulationDistance));
            System.setProperty("minestom.terminal.disabled",
                    String.valueOf(ServerProperties.baseSettings.terminalEnabled));
        } catch (IOException e) {
            System.err.println("Failed to initialize server configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }

        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.LOGGER.info("Initializing NFServer " + VERSION);
        MinecraftServer.setTerminalEnabled(ServerProperties.baseSettings.terminalEnabled);

        lightEngine = new LightEngine();

        InstanceRegister.register(MinecraftServer.getInstanceManager());
        EventRegister.register(MinecraftServer.getGlobalEventHandler());
        CommandRegister.register(MinecraftServer.getCommandManager());

        /* TODO different difficulty for instances */
        MinecraftServer.setDifficulty(ServerProperties.gameSettings.difficulty);

        if (ServerProperties.baseSettings.broadcastToLan) {
            OpenToLAN.open();
        }

        if (ServerProperties.gameSettings.fluidsExtensionEnabled) {
            MinestomFluids.init();
            MinecraftServer.getGlobalEventHandler().addChild(MinestomFluids.events());
        }

        switch (ServerProperties.baseSettings.connectionMode) {
            case ONLINE -> MojangAuth.init();
            case BUNGEECORD -> BungeeCordProxy.enable();
            case VELOCITY -> VelocityProxy.enable(ServerProperties.baseSettings.connectionModeSecret);
        }

        MinecraftServer.LOGGER.info("Using " + ServerProperties.baseSettings.connectionMode + " connection mode");

        minecraftServer.start(ServerProperties.baseSettings.serverIp, ServerProperties.baseSettings.serverPort);
        MinecraftServer.LOGGER.info("Server started at " +
                ServerProperties.baseSettings.serverIp + ":" +
                ServerProperties.baseSettings.serverPort);
    }

    public static void stop() {
        MinecraftServer.LOGGER.info("Shutting down NFServer " + VERSION);
        InstanceRegister.save();
        System.exit(0);
    }

    public static void restart() throws IOException {
        MinecraftServer.LOGGER.info("Restarting NFServer " + VERSION);
        InstanceRegister.save();
        if (Files.exists(Path.of(ServerProperties.baseSettings.restartScript))) {
            Runtime.getRuntime().exec(ServerProperties.baseSettings.restartScript);
        } else {
            MinecraftServer.LOGGER.warn("Restart script does not exist, doing nothing then.");
        }
        System.exit(0);
    }
}