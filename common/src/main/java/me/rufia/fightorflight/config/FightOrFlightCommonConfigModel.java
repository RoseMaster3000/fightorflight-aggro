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
    public boolean do_pokemon_attack_unprovoked = false;
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
    @Comment("Forms that will always be aggressive")
    public String[] always_aggro_aspects = {
            "alolan"
    };
    @Comment("Pokemon that will always be aggressive")
    public String[] always_aggro = {
            "mankey",
            "primeape"
    };
    @Comment("Pokemon that will never be aggressive, priority over always aggresive species/forms")
    public String[] never_aggro = {
            "slowpoke",
            "pyukumuku"
    };
    @Comment("Pokemon that will be aggressive only when provoked(WIP)")
    public String[] provoke_only_aggro = {

    };
    @Comment("Pokemon that will always flee away from the player, priority over always aggresive species/forms")
    public String[] always_flee = {
            "wimpod"
    };
    @Comment("Abilities that will reduce wild pokemon's aggro")
    public String[] aggro_reducing_abilities = {
            "intimidate",
            "unnerve",
            "pressure"
    };
    @Comment("Abilities that works like Mold Breaker(unused)")
    public String[] mold_breaker_like_ablilities = {
            "moldbreaker",
            "turboblaze",
            "teravolt"
    };
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
    @Comment("Will the wild pokemon cries for several times when angered,set to false so the pokemon will only cry one time when it's angered")
    public boolean multiple_cries = true;
    @Comment("Tick(1/20s by default) needed for the pokemon to cry again(it will only work when the multiple_cries is set to true)")
    public int time_to_cry_again = 100;
    @ConfigEntry.Category("Pokemon yield")
    @Comment("The exp. a pokemon can get by killing a pokemon without a battle")
    public float experience_multiplier = 0.5f;
    @Comment("Your pokemon can gain ev by killing a pokemon without a battle")
    public boolean can_gain_ev = true;
    @Comment("If the Pokemon can evolve by using the move out of a Pokemon Battle")
    public boolean can_progress_use_move_evoluiton = true;
    @ConfigEntry.Category("Pokemon Damage and Effects")
    @Comment("The  damage a pokemon would do on hit if it had 0 ATK")
    public float minimum_attack_damage = 1.0f;
    @Comment("The  damage a pokemon would do on hit if it had reached the max stat")
    public float maximum_attack_damage = 10.0f;
    @Comment("Attack stat required to reach the maximum damage,the default value is calculated with 50 level, 130 stat, 252 EVs, IVs of 31, and a helpful nature.")
    public int maximum_attack_stat = 200;
    @Comment("The movement speed multiplier of a pokemon if the Spe stat of this Pokemon is 0.")
    public float minimum_movement_speed = 1.3f;
    @Comment("The movement speed multiplier of a pokemon if the Spe stat of this Pokemon reaches the value in the config.")
    public float maximum_movement_speed = 2.0f;
    @Comment("The speed stat required for a pokemon to reach the highest fleeing and pursuing speed.The default value(548) is the max speed stat of a lvl.100 Regieleki with a beneficial nature.")
    public int speed_stat_limit = 548;
    @Comment("The maximum damage reduction a pokemon can get from its defense/special defense(uses the highest one)")
    public float max_damage_reduction_multiplier = 0.4f;
    @Comment("The highest defense stat needed to get the highest damage reduction.")
    public int defense_stat_limit = 161;
    @Comment("When a player owned Pokemon hurts or is hurt by a wild pokemon, should a pokemon battle be started?")
    public boolean force_wild_battle_on_pokemon_hurt = false;
    @Comment("When a player owned Pokemon hurts or is hurt by another player's pokemon, should a pokemon battle be started? (EXPERIMENTAL)")
    public boolean force_player_battle_on_pokemon_hurt = false;
    @ConfigEntry.Category("Pokemon Ranged Attack")
    @Comment("If wild pokemon can use the ranged attack.")
    public boolean wild_pokemon_ranged_attack = false;
    @Comment("The minimum time for pokemon to prepare(s).")
    public float minimum_ranged_attack_interval = 1.0f;
    @Comment("The maximum time for pokemon to prepare(s).")
    public float maximum_ranged_attack_interval = 3.0f;
    @Comment("The amount of damage a pokemon would do on contact if it had 0 Sp.ATK.")
    public float minimum_ranged_attack_damage = 1.0f;
    @Comment("The amount of damage a pokemon would do on contact if it had reached the max stat")
    public float maximum_ranged_attack_damage = 10.0f;
    @Comment("Special attack stat required to reach the maximum damage,the default value is calculated with 50 level, 130 stat, 252 EVs, IVs of 31, and a helpful nature.")
    public int maximum_special_attack_stat = 200;
    @ConfigEntry.Category("Pokemon Damage Multiplier(misc),These modifiers doesn't stack currently")
    @Comment("Water type damage will be more effective on mobs like Blaze,Enderman,etc.")
    public float water_type_super_effective_dmg_multiplier = 2.0f;
    @Comment("Fire type damage will be not very effective against fire immune entity.(set to 0 if you want a complete immune)")
    public float fire_type_no_effect_dmg_multiplier = 0.1f;
    @Comment("Ice type damage will be not very effective against entity that can't be frozen.(set to 0 if you want a complete immune)")
    public float ice_type_no_effect_dmg_multiplier = 0.1f;
    @Comment("Ice type damage will be more effective against entity that has a weaker resistant to the frost damage.")
    public float ice_type_super_effective_dmg_multiplier = 2.0f;
    @Comment("Poison type damage will be not very effective against undead mobs.(set to 0 if you want a complete immune)")
    public float poison_type_no_effect_dmg_multiplier = 0.1f;
    @ConfigEntry.Category("Health Calculation & Synchronization")
    @Comment("If the original updateMaxHealth() will be replaced by my version. This version uses the hp stat instead of the base hp stat.The following configurations needs this one to work.")
    public boolean shouldOverrideUpdateMaxHealth = true;
    @Comment("The minimum hp of a pokemon entity,shedinja is set to 1.0 and can't be changed.")
    public int min_HP = 8;
    @Comment("The medium hp value of a pokemon entity,the medium value is designed to allow you to better tweak the growth of HP value for the entity")
    public int mid_HP = 40;
    @Comment("The maximum hp of a pokemon entity.")
    public int max_HP = 100;
    @Comment("HP above this value will increase the HP of the entity")
    public int min_HP_required_stat = 20;
    @Comment("The medium hp value of a pokemon entity.")
    public int mid_HP_required_stat = 160;
    @Comment("The maximum hp of a pokemon entity.The max hp of a Blissey is 714.")
    public int max_HP_required_stat = 500;
    @ConfigEntry.Category("Poke Staff")
    public boolean stay_after_move_command = true;

}