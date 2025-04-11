## RM3-Aggro Edition
Unprovoked Attack Improved! My version works in a more predictable way. There are 3 lists of pokemon in the config
1. Aggressive Pokemon (attack on sight, unprovoked)
2. Territorial Pokemon (attack only if player provokes)
3. Wimp Pokemon (always run if player provokes)
0. Smart Pokemon (all others, will never attack on sight, but will fight/flight if provoked)

There is also these key settings
* always_aggro_below [INT] (y level where Pokémon will always aggro, EXPECT wimp pokemon)
* territorial_nocturnal [BOOL] (territorial pokemon converted to Aggressive at night)
* smart_nocturnal [BOOL] (Smart Pokémon converted to Territorial at night)
* sunlight_wimps [BOOL] (dark & ghost Pokémon converted to wimp in light)


## Features included in v0.7.4:
- **Unprovoked Attack Disabled** I personally don't like this feature because the Pokemon spawns anywhere and anytime.It's quite easy to get attacked when you are doing something. However,you are free to enable it in the config.
- **Lower Pokemon Damage:** I noticed that some players commented on the curseforge page that the pokemon damage was too high ,so I lowered the default value of the maximum damage.The stat required to reach the maximum damage is also lowered to suit the lower damage.You are free to use the config to adjust the damage.
- **Configurable aggresion:** Added a multiplier so that you can multiply the level of the pokemon when calculating its aggresion.
- **Faster Pokemon:** Pokemon with a higher speed stat can run faster.(can be changed in the config, the base speed of cobblemon is too slow, so it might not be obvious.)
- **Range attack!:** Added a range attack for pokemon whose Sp.ATK is higher than its ATK.
  - Wild pokemon are not allowed to use the range attack.(can be enabled in the config)
- **Different ways of range attack:** If a pokemon has some special moves,they will shoot different bullet.
  - The moves' type and power will influence the projectile's if the moves is a special move.However, if your pokemon doesn't have these moves, the type of the projectile will be based on the pokemon's primary type and the power will be set to 60(can be changed in the config).
  - Most ball and bomb moves can cause a small explosion.
  - Sound based moves and some blast moves shoots the target like the vanilla Guardian,the damage is slightly lower,but it's hard to miss.
  - The classification of the moves is not very strict.(e.g. Oblivion wing uses the mechanic of the blast moves because Yveltal shoots a beam when using this move in the core series.) It is written in the config,you can edit it yourself.
- The player's Pokemon prefers to use the first move in the move set.You can use the Poke Staff to select the move you want to use, even forcing a special attacker to melee!(use JEI to check the recipe)
- **Special effect for moves** 
  - The panicked pokemon can teleport to a nearby position if it learns the teleport move.(can be disabled in the config)
  - Player's pokemon will be recalled automatically when using moves like U-turn and hitting the target(melee)/shooting(range)
  - Explosive moves can cause an explosion.
  - HP draining moves can heal your Pokemon from the damage they dealt.
  - If a special attack move is a contact move,the Pokemon will use the melee instead of shooting projectiles.However the damage calculation still uses the Special Attack stat.(e.g. Draining Kiss) 
  - Most of the physical ball and bomb moves shoot a projectile instead of melee,it still uses the Attack stat to calculate the damage.(Both of the Ice Ball and Magnet Bomb are  ball and bomb moves.But I don't think Ice Ball shoots anything according to its description,while Magnet Bomb truly shoots a bomb.So using Ice Ball don't shoot anything.)
- **Mobs killed by your pokemon will drop items and experience like it was killed by a tamed wolf.**
- Your pokemon can gain experience and ev by killing pokemon without starting a pokemon battle(needs to be **the last mob** that deals the damage,can be disabled in the config)
- Your pokemon can evolve by using the move to hit other mobs instead of starting a Pokemon Battle(e.g. Primeape needs to use Rage Fist 20 times to evolve,now you can do it without a traditional Pokemon Battle)(Configurable).
- Adds the Oran Lucky Egg(held item) to gain more experience from pokemon killed by your pokemon,right-click your pokemon while sneaking to give the item to the pokemon.(**The Oran Lucky Egg won't give you extra xp from any other ways!**)
- **Animation Support** Support for animations from cobblemon mod when attacking(These animations are not designed for this mod so it might be weird)
- Your pokemon can taunt the pokemon that attacks you.(Your pokemon can't be taunted.)
- - Moves(taunt,follow me,rage powder,torment) are needed,and they need to be set as the first move in the move set. to taunt the wild pokemon.(can be disabled in the config)
- Pokemon with higher defense stat can lower the damage it take.(configurable)
- A new hotkey to let your pokemon start a battle with the pokemon that tries to attack you.(not working currently)
- Some abilities(intimidate,unnerve,pressure) can lower the nearby pokemon's aggro.
- The Wimpod line Pokemon will be recalled when taking damage and the health is below 50%.
- Moves like earthquake that hurts all pokemon around will hurt the entities nearby when the Pokemon hits the target.(The special moves will use the melee attack,too.)  
- **Type effectiveness for some non-pokemon entities** These effects don't strictly follow the rules of Pokemon,instead,they are more closely related to minecraft itself:
  * Water type damage will be more effective on mobs that takes damage from the water.
  * Fire type damage will be not very effective against fire immune entities(x0.1 by default,set to 0 might be confusing for new player).
  * Ice type damage will be not very effective against entities that don't take the frost damage(still x0.1).
  * Ice type damage will be more effective against entities that has a weaker resistant to the frost damage,(blaze,magma cube,etc.).
  * Poison type damage will be not very effective against undead mobs.(x0.1 again)
* Health mechanic reworked
  * Use a mixin to replace the original max health calculation function,set shouldOverrideUpdateMaxHealth in config/fightorflight.json5 to false to disable it if you don't like the changes I made.
  * The hp of the pokemon entity is no longer decided by the base stat,it is decided by the hp stat of the pokemon directly now.
  * The damage a Pokemon entity takes will be dealt to the pokemon,causing the hp to drop,healing the entity will also heal the pokemon(Recommended to use with [Healing Campfire](https://modrinth.com/mod/healing-campfire)). (This feature uses some function used by my max health calculation function. Disabling the function will also disable this feature.)
* Some held item will offer damage boost(type-enhancing items,Choice Specs, Choice Band,Muscle Band,Choice Glasses,Life orb)
  * The life orb will also deal damage to the pokemon like the core series.A pokemon is immune to the damage if the pokemon's ability is Sheer Force or Magic Guard.
* The sheer force will boost all moves' damage,but the move can no longer apply effects to the entities.(e.g. fire type move will no longer set the entity on fire)
* Balance change: increase the maximum damage and increased the stat requirement slightly to encourage to player to try damage-enhancing items more at the early game.
* Ball projectiles will cause an explosion when hitting something. Explosion caused by fire type moves will ignite the ground.(I personally doesn't like mob griefing, so I disabled it by default.You need to enable it manually. Sorry about that.)
* More aggro configurations
* Pokemon entity is immune to the suffocation damage.(can be disabled in the config)
* Pokemon stops running away if the health isn't full(can be disabled in the config).
* Added multipliers for player's Pokemon
* Added a new config to disable the health sync for wild pokemon(the health sync for wild pokemon will be disabled by default)
- Options to enable friendly fire
- Nature multipliers.(It can't be edited in the config before.)
- Failed captures will be counted as provocation.
- Strength and weakness can influence the pokemon's damage now(follow the Minecraft rule, mostly). Both the range attack and melee attack can benefit from this(I know these effects don't increase the damage of the projectiles in Minecraft.)

## Features/Changes that is not released currently:
- New config options: The aggression system will only work in the dark areas.(Similar to the spiders in Minecraft)
- Bug fixes: The invulnerable time don't work properly for pokemon entity;
- Bug fixes: The tracing projectiles should work correctly now.

## Dependencies
[Architectury](https://modrinth.com/mod/architectury-api) required


## TODO
- Main Goal For the Next Update:
  - Combat overhaul
    - Remove the type effect(levitate for psychic, weakness for fight,etc.)
    - Give more special effect to different moves(Pokemon gain strength after using Power-up Punch, gain weakness after using Close Combat)
      - I want to make it easy so the effect level won't stack
- Things that might be done in a short period of time(1~3 big updates):
  - Special effect for status moves. (Most of the status moves has no effect currently,they can be used as a way to make your pokemon passive.)
    - I want to encourage the player to use Poke Staff to switch the move they use if they want higher damage / tactical advantages
    - Example: Pokemon gain strength when using Swords Dance, the duration will be longer than using Power-up Punch, and the cooldown will be longer / Using Telekinesis will levitate the target.
    - The indicator for the cooldown might come later than this feature.
  - Type effectiveness for Pokemon entities:There doesn't seem to be a built-in type modifier list in Cobblemon,the data might be sent to showdown to process,which means I should spend some time to add it myself and test it.
- Things that might not be done in a short period of time:
  - Repel effect.(It's being worked on by the Cobblemon team according to their roadmap.)
  - Attack Position for Poke Staff(not available currently. It is quite difficult.)
  - Limit the number of pokemon in combat(It's a little bit difficult to check)
- Long term plans:
  - Give more special effects to different moves.
  - Special effect for abilities like aftermath,bulletproof,soundproof,etc.
  - More config options for fight of flight choices.
  - The pp of the pokemon moves will be consumed after using it outside the battle(this feature could make the pokemon obviously weaker at the early game and the moves don't need to be balanced that way currently, so I won't work on it until the mod got cool enough.)

## Known Issues
- Damage of moves that needs the stat like electro ball can't be calculated properly.(I don't know how to find a proper way to calculate the damage if this move is used to attack a non-Pokemon mob,so I won't fix it until I figure out a way.Sorry about that.)

## Known issues for 1.21.1 version
- I'm not sure why the battle does not start when using the key bind I added.

## How to use the Poke Staff
1. Get one Poke Staff by crafting or get it in the creative mode.
2. Sneak+Right click: Switch the mode of the staff  
Right click:Send the command to the pokemon(send command mode)/Select the move(select mode)/select the command(select command mode)
