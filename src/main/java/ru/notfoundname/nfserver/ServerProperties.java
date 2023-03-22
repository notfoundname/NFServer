package ru.notfoundname.nfserver;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.io.File;
import java.io.IOException;

public final class ServerProperties {

    public static final File serverFile = new File(".", "server-properties.conf");
    public static HoconConfigurationLoader loader;
    public static CommentedConfigurationNode rootNode;

    public static BaseSettings baseSettings;
    public static GameSettings gameSettings;
    public static Translations translations;

    public static void reload() throws IOException {
        if (!serverFile.exists()) {
            try {
                serverFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loader = HoconConfigurationLoader.builder()
                .path(serverFile.toPath())
                .build();
        rootNode = loader.load();

        baseSettings = rootNode.node("base-settings").get(BaseSettings.class);
        gameSettings = rootNode.node("minestom-settings").get(GameSettings.class);
        translations = rootNode.node("translations").get(Translations.class);

        rootNode.node("base-settings").set(BaseSettings.class, baseSettings);
        rootNode.node("game-settings").set(GameSettings.class, gameSettings);
        rootNode.node("translations").set(Translations.class, translations);

        loader.save(rootNode);
    }

    @ConfigSerializable
    public static class BaseSettings {
        public String serverIp = "0.0.0.0";
        public int serverPort = 25565;
        @Comment("Minimessage format. To set server logo, put 64x64 icon.png near server core.")
        public String motd = "A Minecraft NFServer";
        public int maxPlayers = 20;
        public String connectionMode = "offline";
        public String connectionModeSecret = "";
        @Comment("Don't show nicknames when you hover on player number.")
        public boolean hideOnlinePlayers = false;
        public boolean broadcastToLan = true;
        @Comment("Currently non-functional")
        public boolean whiteList = false;
        //public boolean whiteListActsAsBlackList = false;
        public String operatorPermission = "nfserver.operator";
        public boolean terminalEnabled = true;
        public String restartScript = "start.sh";
    }

    @ConfigSerializable
    public static class GameSettings {
        @Comment("Default world (or instance) that will be used for joined players.")
        public String levelName = "world";
        @Comment("Possible values: peaceful, easy, normal, hard")
        public String difficulty = "easy";
        @Comment("Possible values: survival, creative, adventure, spectator")
        public String gamemode = "survival";
        public int viewDistance = 10;
        public int viewSimulationDistance = 10;
        public boolean pvpExtensionEnabled = true;
        //public boolean pvpExtensionOldSystem = false;
    }

    @ConfigSerializable
    public static class Translations {
        public String unknownCommand = "Unknown command! Contact server administrator if this is a problem.";
        public String serverWhitelisted = "Server is whitelisted";
    }
}
