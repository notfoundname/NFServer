package ru.notfoundname.notfoundserver;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.io.File;
import java.io.IOException;

public final class ServerProperties {

    public static final File serverFile = new File(".", "server-properties.json");
    public static GsonConfigurationLoader loader;
    public static BasicConfigurationNode rootNode;
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
                .path(serverFile.toPath())
                .build();

        rootNode = loader.load();
        config = rootNode.get(Properties.class);
        rootNode.set(Properties.class, config);
        loader.save(rootNode);
    }

    @ConfigSerializable
    public static class Properties {
        @Comment("test")
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

        public String difficulty = "easy";
        public int viewDistance = 10;
        public int simulationDistance = 10;
        public boolean pvp = true;
    }
}
