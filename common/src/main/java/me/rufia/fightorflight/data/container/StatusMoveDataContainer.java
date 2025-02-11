package me.rufia.fightorflight.data.container;

import me.rufia.fightorflight.data.MoveDataContainer;
import me.rufia.fightorflight.data.movedatas.StatusMoveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusMoveDataContainer extends MoveDataContainer<StatusMoveData> {
    public StatusMoveDataContainer(String type, String target, float chance, boolean canActivateSheerForce, String name, List<String> move_list) {
        super(type, target, chance, canActivateSheerForce, name, move_list);
    }

    @Override
    public Map<String, StatusMoveData> build() {
        Map<String, StatusMoveData> dataMap = new HashMap<>();
        for (String moveName : getMoveList()) {
            StatusMoveData data = new StatusMoveData(getTarget(), getChance(), canActivateSheerForce(), getName());
            dataMap.put(moveName, data);
        }
        return dataMap;
    }
}
