package me.rufia.fightorflight.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "fightorflight_moves")
public class FightOrFlightMoveConfigModel implements ConfigData {
    @ConfigEntry.Category("Special attack moves")
    @Comment("The multiplier of the move power in calculating damage(The final damage can't be higher than the value in the config)")
    public float move_power_multiplier = 1.0f;
    @Comment("The multiplier for the moves that shoots a beam when calculating damage(The final damage can't be higher than the value in the config.These moves are hard to avoid in an open area so the damage should be slightly lower than the others)")
    public float beam_move_power_multiplier = 0.8f;
    @Comment("If a pokemon doesn't have the correct moves to use,the base power will be used to calculate the damage.")
    public int base_power = 60;
    @Comment("The minimum radius of the AoE moves")
    public float min_AoE_radius = 2.0f;
    @Comment("The maximum radius of the AoE moves")
    public float max_AoE_radius = 3.0f;
    @Comment("The radius of the status moves")
    public float status_move_radius=8.0f;
    @Comment("Taunting moves are needed to taunt the aggressive wild pokemon")
    public boolean taunt_moves_needed=true;
    @Comment("Wild pokemon can taunt your pokemon(WIP)")
    public  boolean wild_pokemon_taunt=false;
    @Comment("Moves that shoots multiple bullet")//TODO
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
    @Comment("Moves that shoots single beam or pulse")
    public String[] single_beam_moves = {
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
            "electroshot",
            "photongeyser",

            "flamethrower"
    };
    @Comment("Moves that ignores abilities(unused)")
    public String[] mold_breaker_like_moves={
            "sunsteelstrike",
            "moongeistbeam",
            "photongeyser"
    };
    @Comment("Moves that start an explode")
    public String[] explosive_moves = {
            "self-destruct",
            "explosion",
            "mindblown",
            "mistyexplosion"
    };
    @Comment("Moves that use sound to attack")
    public String[] sound_based_moves = {
            "snore",
            "uproar",
            "hypervoice",
            "bugbuzz",
            "chatter",
            "round",
            "echoedvoice",
            "relicsong",
            "snarl",
            "disarmingvoice",
            "boomburst",
            "sparklingaria",
            "clangingscales",
            "clangoroussoulblaze",
            "overdrive",
            "eeriespell",
            "torchsong",
            "alluringvoice",
            "psychicnoise"
    };
    @Comment("Moves that can switch your pokemon")
    public String[] switch_moves = {
            "teleport",
            "batonpass",
            "uturn",
            "partingshot",
            "voltswitch",
            "flipturn"
    };
    @Comment()
    public String[] recoil_moves_allHP = {
            "self-destruct",
            "explosion",
            "mindblown",
            "mistyexplosion"
    };
    //TODO
    @ConfigEntry.Category("Status  moves(To do)")
    @Comment("Moves that taunt other pokemon")
    public String[] taunting_moves={
            "taunt",
            "followme",
            "ragepowder",
            "torment"
    };
    @Comment("Moves that burns the pokemon(WIP)")
    public String[] burn_status_move = {
            "willowisp"
    };
}
