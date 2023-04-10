package ru.notfoundname.nfserver.instances;

import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import ru.notfoundname.nfserver.ServerProperties;

import java.io.IOException;
import java.nio.file.Files;

public class DefaultInstance {
    private static Instance instance;

    public static void initialize(InstanceManager instanceManager) {

        if (!Files.exists(ServerProperties.gameSettings.levelName)) {
            try {
                Files.createDirectory(ServerProperties.gameSettings.levelName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        IChunkLoader loader = new AnvilLoader(ServerProperties.gameSettings.levelName);

        instance = instanceManager.createInstanceContainer(ServerProperties.gameSettings.dimension, loader);
    }

    public static Instance get() {
        return instance;
    }

    public static void save() {
        instance.saveInstance();
        instance.saveChunksToStorage();
    }
}
