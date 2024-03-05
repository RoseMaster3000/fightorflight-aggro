package me.rufia.fightorflight.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "fightorflight_visual_effect")
public class FightOrFlightVisualEffectConfigModel implements ConfigData {
    @Comment("Moves that spawn angry_villager particle around the pokemon")
    public String[] self_angry_moves = {
            "partingshot",
            "lashout",
            "ragingfury",
            "thrash",
            "outrage",
            "temperflare"
    };
    @Comment("Moves that spawn soul_fire_flame particle around the pokemon's target")
    public String[] target_soul_fire_moves = {
            "willowisp",
            "infernalparade",
            "bitterblade"
    };
    @Comment("Moves that spawn soul particle around the pokemon's target")
    public String[] target_soul_moves = {
            "nightshade",
            "astralbarrage"
    };
}
