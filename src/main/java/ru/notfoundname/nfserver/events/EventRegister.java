package ru.notfoundname.nfserver.events;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerListPingEvent;
import ru.notfoundname.nfserver.NFServer;
import ru.notfoundname.nfserver.ServerProperties;
import ru.notfoundname.nfserver.instances.DefaultInstance;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class EventRegister {
    public static void register(GlobalEventHandler globalEventHandler) {
        /* TODO there is probably some fancier way */
        globalEventHandler.addListener(ServerListPingEvent.class, event -> {
            event.getResponseData().setDescription(
                    MiniMessage.miniMessage().deserialize(ServerProperties.baseSettings.motd));
            event.getResponseData().setMaxPlayer(ServerProperties.baseSettings.maxPlayers);
            event.getResponseData().setPlayersHidden(ServerProperties.baseSettings.hideOnlinePlayers);
            if (Files.exists(Path.of("./server-icon.png"), LinkOption.NOFOLLOW_LINKS)) {
                event.getResponseData().setFavicon("data:image/png;base64,server-icon.png");
            }
        });
        globalEventHandler.addListener(AsyncPlayerPreLoginEvent.class, event -> {
            if (ServerProperties.baseSettings.whiteList) {
                event.getPlayer().kick(
                        MiniMessage.miniMessage().deserialize(ServerProperties.translations.serverWhitelisted));
            }
        });
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(DefaultInstance.get());
            try {
                player.setGameMode(ServerProperties.gameSettings.gamemode);
            } catch (IllegalArgumentException e) {
                MinecraftServer.LOGGER.warn("Unknown gamemode " + ServerProperties.gameSettings.gamemode);
                player.setGameMode(GameMode.ADVENTURE);
            }
            if (ServerProperties.gameSettings.setSkinsBasedOnNickname) {
                player.setSkin(PlayerSkin.fromUsername(player.getUsername()));
            }
            player.setRespawnPoint(new Pos(0, 30, 0));
        });
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event ->
                NFServer.lightEngine.recalculateChunk(event.getPlayer().getChunk()));
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event ->
                NFServer.lightEngine.recalculateChunk(event.getPlayer().getChunk()));
        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event ->
                NFServer.lightEngine.recalculateChunk(event.getPlayer().getChunk()));
        globalEventHandler.addListener(PlayerChatEvent.class, event ->
                MinecraftServer.LOGGER.info(event.getPlayer().getName() + " - " + event.getMessage()));
    }
}
