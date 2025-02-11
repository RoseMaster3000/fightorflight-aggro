package me.rufia.fightorflight.data.container;

import me.rufia.fightorflight.data.MoveDataContainer;
import me.rufia.fightorflight.data.movedatas.StatChangeMoveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatChangeMoveDataContainer extends MoveDataContainer<StatChangeMoveData> {
    private int stage;

    public StatChangeMoveDataContainer(String type, String target, float chance, boolean canActivateSheerForce, String name, List<String> move_list, int stage) {
        super(type, target, chance, canActivateSheerForce, name, move_list);
        this.stage = stage;
    }

    @Override
    public Map<String, StatChangeMoveData> build() {
        Map<String, StatChangeMoveData> dataMap = new HashMap<>();
        for (String moveName : getMoveList()) {
            StatChangeMoveData data = new StatChangeMoveData(getTarget(), getChance(), canActivateSheerForce(), getName(), stage);
            dataMap.put(moveName, data);
        }
        return dataMap;
    }
}
