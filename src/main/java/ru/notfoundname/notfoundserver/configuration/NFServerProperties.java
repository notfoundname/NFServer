package ru.notfoundname.notfoundserver.configuration;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.File;
import java.io.IOException;

public final class NFServerProperties {

    public static File serverFile = new File(".", "server.json");
    public static GsonConfigurationLoader loader;
    public static BasicConfigurationNode node;
    public static Properties config;

    public static void initialize() throws IOException {

        if (!serverFile.exists()) {
            try {
                serverFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loader = GsonConfigurationLoader.builder().path(serverFile.toPath()).build();
        node = loader.load();
        config = node.get(Properties.class);
        node.set(Properties.class, config);
        loader.save(node);
    }

    @ConfigSerializable
    public static class Properties {
        public String serverIp = "0.0.0.0";
        public int serverPort = 25565;
        public String motd = "A Minecraft NFServer";
        public int maxPlayers = 20;
        public ConnectionMode connectionMode = ConnectionMode.OFFLINE;
        public String connectionModeSecret = "secret";
        public boolean hideOnlinePlayers = false;
        public boolean broadcastToLan = true;
        public boolean whiteList = false;
        public boolean whiteListActsAsBlackList = false;
        public String levelName = "world";
        public int viewDistance = 10;
        public int simulationDistance = 10;
        public boolean pvp = true;
    }

    @ConfigSerializable
    public enum ConnectionMode {
        OFFLINE,
        ONLINE,
        BUNGEECORD,
        VELOCITY
    }
}
