package ru.notfoundname.notfoundserver;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.File;
import java.io.IOException;

public final class ServerProperties {

    public static File serverFile = new File(".", "server.json");
    public static GsonConfigurationLoader loader;
    public static ConfigurationNode rootNode;
    public static Properties config;

    public static void initialize() throws IOException {

        if (!serverFile.exists()) {
            try {
                serverFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loader = GsonConfigurationLoader.builder()
                .path(serverFile.toPath()) // or url(), or source/sink
                .build();

        rootNode = loader.load(); // Load from file
        config = rootNode.get(Properties.class); // Populate object

        rootNode.set(Properties.class, config); // Update the backing node
        loader.save(rootNode); // Write to the original file
    }

    @ConfigSerializable
    public static class Properties {
        public String serverIp = "0.0.0.0";
        public int serverPort = 25565;
        public String motd = "A Minecraft NFServer";
        public int maxPlayers = 20;
        public String connectionMode = "offline";
        public String connectionModeSecret = "secret";
        public boolean hideOnlinePlayers = false;
        public boolean broadcastToLan = true;
        public boolean whiteList = false;
        public boolean whiteListActsAsBlackList = false;
        public boolean allowAllPlayersToExecuteDefaultCommands = false;
        public String levelName = "world";
        public int viewDistance = 10;
        public int simulationDistance = 10;
        public boolean pvp = true;
    }
}
