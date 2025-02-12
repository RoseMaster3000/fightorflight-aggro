package me.rufia.fightorflight.data.container;

import me.rufia.fightorflight.data.MoveDataContainer;
import me.rufia.fightorflight.data.movedatas.StatusEffectMoveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusEffectMoveDataContainer extends MoveDataContainer<StatusEffectMoveData> {
    public StatusEffectMoveDataContainer(String type, String target, float chance, boolean canActivateSheerForce, String name, List<String> move_list) {
        super(type, target, chance, canActivateSheerForce, name, move_list);
    }

    @Override
    public Map<String, StatusEffectMoveData> build() {
        Map<String, StatusEffectMoveData> dataMap = new HashMap<>();
        for (String moveName : getMoveList()) {
            StatusEffectMoveData data = new StatusEffectMoveData(getTarget(), getChance(), canActivateSheerForce(), getName());
            dataMap.put(moveName, data);
        }
        return dataMap;
    }
}
