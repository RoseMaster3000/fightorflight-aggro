package me.rufia.fightorflight.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "fightorflight")
public class FightOrFlightCommonConfigModel implements ConfigData {

    @ConfigEntry.Category("Wild Pokemon Aggression")
    @Comment("Do more aggressive Pokemon fight back when provoked?")
    public boolean do_pokemon_attack = true;
    @Comment("Do especially aggressive Pokemon attack unprovoked?")
    public boolean do_pokemon_attack_unprovoked = true;
    @Comment("Do aggro Pokemon attack their targets even if they're in the middle of a battles?")
    public boolean do_pokemon_attack_in_battle = false;
    @Comment("The minimum level a Pokemon needs to be to fight back when provoked.")
    public int minimum_attack_level = 5;
    @Comment("The minimum level a Pokemon needs to be to attack unprovoked.")
    public int minimum_attack_unprovoked_level = 10;
    @Comment("The multiplier used to calculate the wild Pokemon's aggression,a low value can make the high level Pokemon less aggressive")
    public float aggression_level_multiplier = 1.0f;
    @Comment("Are Dark types more aggressive at or below light level 7 and less aggressive at or above light level 12")
    public boolean dark_light_level_aggro = true;
    @Comment("Are Ghost types more aggressive at or below light level 7 and less aggressive at or above light level 12")
    public boolean ghost_light_level_aggro = true;
    @Comment("Pokemon that will always be aggressive")
    public String[] always_aggro = {
            "mankey",
            "primeape"
    };
    @Comment("Pokemon that will never be aggressive")
    public String[] never_aggro = {};
    @Comment("Allow the Pokemon to use the teleport move to flee if the Pokemon had learnt it.")
    public boolean allow_teleport_to_flee = true;

    @ConfigEntry.Category("Player Pokemon Defence")
    @Comment("Do player Pokemon defend their owners when they attack or are attacked by other mobs?")
    public boolean do_pokemon_defend_owner = true;
    @Comment("Do player Pokemon defend their owners proactively? (follows the same rules as Iron Golems)")
    public boolean do_pokemon_defend_proactive = true;
    @Comment("Can player Pokemon target other players? (EXPERIMENTAL)")
    public boolean do_player_pokemon_attack_other_players = false;
    @Comment("Can player Pokemon target other player's Pokemon? (EXPERIMENTAL)")
    public boolean do_player_pokemon_attack_other_player_pokemon = false;

    @ConfigEntry.Category("Pokemon Damage and Effects")
    @Comment("The amount of damage a pokemon would do on contact if it had 0 ATK and Sp.ATK.")
    public float minimum_attack_damage = 1.0f;
    @Comment("The amount of damage a pokemon would do on contact if it had 255 ATK or Sp.ATK.")
    public float maximum_attack_damage = 25.0f;
    @Comment("The movement speed multiplier of a pokemon if the Spe stat of this Pokemon is 0.")
    public float minimum_movement_speed = 1.0f;
    @Comment("The movement speed multiplier of a pokemon if the Spe stat of this Pokemon reaches the value in the config.")
    public float maximum_movement_speed = 2.0f;
    @Comment("The speed stat required for a pokemon to reach the highest fleeing and pursuing speed.The default value(548) is the max speed stat of a lvl.100 Regieleki with a beneficial nature.")
    public int speed_stat_limit = 548;
    @Comment("When a player owned Pokemon hurts or is hurt by a wild pokemon, should a pokemon battle be started?")
    public boolean force_wild_battle_on_pokemon_hurt = false;
    @Comment("When a player owned Pokemon hurts or is hurt by another player's pokemon, should a pokemon battle be started? (EXPERIMENTAL)")
    public boolean force_player_battle_on_pokemon_hurt = false;

}