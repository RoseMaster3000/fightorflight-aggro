package me.rufia.fightorflight.utils.listeners;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.data.MoveData;
import me.rufia.fightorflight.data.MoveDataContainer;
import me.rufia.fightorflight.data.container.StatChangeMoveDataContainer;
import me.rufia.fightorflight.data.container.StatusEffectMoveDataContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoveDataListener extends SimplePreparableReloadListener<Map<ResourceLocation, MoveDataContainer>> {
    public MoveDataListener() {
    }

    @Override
    protected Map<ResourceLocation, MoveDataContainer> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, MoveDataContainer> map = new HashMap<>();
        CobblemonFightOrFlight.LOGGER.info("[FOF] Preparing to read");
        /*
        for (var entry : resourceManager.listResources("fof_move_data/" + "stat", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var resourceLocation = entry.getKey();
            var resource = entry.getValue();
            try {
                //CobblemonFightOrFlight.LOGGER.info(resourceLocation.getPath());
                JsonReader reader = new JsonReader(new InputStreamReader(resource.open()));
                Gson gson = new Gson();
                StatChangeMoveDataContainer container = gson.fromJson(reader, StatChangeMoveDataContainer.class);
                map.put(resourceLocation, container);
            } catch (Exception e) {
                CobblemonFightOrFlight.LOGGER.warn("Failed to read " + resourceLocation);
            }
        }*/
        prepareTag(resourceManager, "stat", StatChangeMoveDataContainer.class, map);
        prepareTag(resourceManager, "status", StatusEffectMoveDataContainer.class, map);
        return map;
    }

    private void prepareTag(ResourceManager resourceManager, String tagName, Type type, Map<ResourceLocation, MoveDataContainer> map) {
        for (var entry : resourceManager.listResources("fof_move_data/" + tagName, fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var resourceLocation = entry.getKey();
            var resource = entry.getValue();
            try {
                //CobblemonFightOrFlight.LOGGER.info(resourceLocation.getPath());
                JsonReader reader = new JsonReader(new InputStreamReader(resource.open()));
                Gson gson = new Gson();
                map.put(resourceLocation, gson.fromJson(reader, type));
            } catch (Exception e) {
                CobblemonFightOrFlight.LOGGER.warn("Failed to read " + resourceLocation);
            }
        }
    }

    private void register(Map<String, ? extends MoveData> dataMap) {
        for (var mapEntry : dataMap.entrySet()) {
            if (MoveData.moveData.containsKey(mapEntry.getKey())) {
                if (MoveData.moveData.get(mapEntry.getKey()) != null) {
                    MoveData.moveData.get(mapEntry.getKey()).add(mapEntry.getValue());
                    //CobblemonFightOrFlight.LOGGER.info("Added an effect");
                }
            } else {
                MoveData.moveData.put(mapEntry.getKey(), new ArrayList<>());
                MoveData.moveData.get(mapEntry.getKey()).add(mapEntry.getValue());
            }
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, MoveDataContainer> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        MoveData.moveData.clear();
        for (var entry : map.entrySet()) {
            CobblemonFightOrFlight.LOGGER.info("[FOF] Move Data is ready to be processed");
            var location = entry.getKey();
            var container = entry.getValue();
            Map<String, ? extends MoveData> dataMap = null;
            if (container instanceof StatChangeMoveDataContainer statChangeMoveDataContainer) {
                dataMap = statChangeMoveDataContainer.build();
            } else if (container instanceof StatusEffectMoveDataContainer statusEffectMoveDataContainer) {
                dataMap = statusEffectMoveDataContainer.build();
            }
            if (dataMap != null) {
                register(dataMap);
            }
        }
        CobblemonFightOrFlight.LOGGER.info("[FOF] Move Data processed.");
    }


}
