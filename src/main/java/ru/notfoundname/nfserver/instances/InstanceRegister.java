package ru.notfoundname.nfserver.instances;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;

public class InstanceRegister {
    public static void register(InstanceManager instanceManager) {
        DefaultInstance.initialize(instanceManager);
        instanceManager.registerInstance(DefaultInstance.get());
    }

    public static void save() {
        MinecraftServer.LOGGER.info("Saving instances");
        MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveInstance);
        MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
    }
}
