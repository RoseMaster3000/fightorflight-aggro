package me.rufia.fightorflight.utils.listeners;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class MoveDataListener implements PreparableReloadListener {

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        //TODO clear the list/set
        for (var entry : resourceManager.listResources("move_data", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var resourceLocation = entry.getKey();
            var resource = entry.getValue();
            try {
                var reader=resource.open();
            } catch (Exception e) {
                CobblemonFightOrFlight.LOGGER.warn("Failed to read " + resourceLocation);
            }
        }
        return null;
    }
}
