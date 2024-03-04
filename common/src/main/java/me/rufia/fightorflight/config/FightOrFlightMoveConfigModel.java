package me.rufia.fightorflight.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "fightorflight_moves")
public class FightOrFlightMoveConfigModel implements ConfigData {
    @ConfigEntry.Category("Special attack moves")
    @Comment("The multiplier of the move power in calculating damage(The final damage can't be higher than the value in the config)")
    public float move_power_multiplier=1.0f;
    @Comment("The multiplier for the moves that shoots a beam when calculating damage(The final damage can't be higher than the value in the config.These moves are hard to avoid in an open area so the damage should be slightly lower than the others)")
    public float beam_move_power_multiplier=0.8f;
    @Comment("If a pokemon doesn't have the correct moves to use,the base power will be used to calculate the damage.")
    public int base_power=60;
    @Comment("Moves that shoots multiple bullet")
    public String[] multiple_bullet_moves = {
            "powergem"
    };
    @Comment("Moves that shoots single bullet")
    public String[] single_bullet_moves = {
            "electroball",
            "focusblast",
            "weatherball",
            "pyroball",
            "acidspray",
            "sludgebomb",
            "mudbomb",
            "pollenpuff",
            "shadowball",
            "searingshot",
            "octazooka",
            "energyball",
            "zapcannon",
            "mistball",
            "syrupbomb",
            "armorcanon",

            "sludge",
            "technoblast"
    };
    @Comment("Moves that shoots multiple tracing bullet")
    public String[] multiple_tracing_bullet_moves = {
            "dracometeor",
            "ancientpower"
    };
    @Comment("Moves that shoots single tracing bullet")
    public String[] single_tracing_bullet_moves = {
            "aurasphere"
    };
    @Comment("Moves that shoots single beam or pulse(WIP)")
    public String[] single_beam_moves={
            "signalbeam",
            "chargebeam",
            "icebeam",
            "eternabeam",
            "solarbeam",
            "moongeistbeam",
            "aurorabeam",

            "waterpulse",
            "darkpulse",
            "dragonpulse",
            "terrainpulse",
            "healpulse",

            "watergun",
            "hydropump",
            "prismaticlaser",
            "lusterpurge",
            "electroshot"
    };
}
