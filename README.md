### [Architectury](https://modrinth.com/mod/architectury-api) required!!!
## New Features & Changes Since 0.5.3
### Features included in [v0.6.1](https://github.com/LyquidQrystal/fightorflight/releases/tag/v0.6.1):
- **Unprovoked Attack Disabled** I personally don't like this feature because the Pokemon spawns anywhere and anytime.It's quite easy to get attacked when you are doing something. However,you are free to enable it in the config.
- **Lower Pokemon Damage:** I noticed that some players commented on the curseforge page that the pokemon damage was too high ,so I lowered the default value of the maximum damage.The stat required to reach the maximum damage is also lowered to suit the lower damage.You are free to use the config to adjust the damage.
- **Configurable aggresion:** Added a multiplier so that you can multiply the level of the pokemon when calculating its aggresion.
- **Faster Pokemon:** Pokemon with a higher speed stat can run faster.(can be changed in the config)
- **Range attack!:** Added a range attack for pokemon whose Sp.ATK is higher than its ATK.
- - Wild pokemon are not allowed to use the range attack.(can be enabled in the config)
- **Different ways of range attack:** If a pokemon has some special moves,they will shoot different bullet.
- - The moves' type and power will influence the projectile's if the moves is a special move.However, if your pokemon doesn't have these moves, the type of the projectile will be based on the pokemon's primary type and the power will be set to 60(can be changed in the config).
- - Most ball and bomb moves can cause a small explosion.
- - Sound based moves and some blast moves shoots the target like the vanilla Guardian,the damage is slightly lower,but it's hard to miss.
- - The classification of the moves is not very strict.(e.g. Oblivion wing uses the mechanic of the blast moves because Yveltal shoots a beam when using this move in the core series.) It is written in the config,you can edit it yourself.
- The player's Pokemon prefers to use the first move in the move set.You can use the Poke Staff to select the move you want to use, even forcing a special attacker to melee!(use JEI to check the recipe)
- **Special effect for moves** 
- - The panicked pokemon can teleport to a nearby position if it learns the teleport move.(can be disabled in the config)
- - Player's pokemon will be recalled automatically when using moves like U-turn and hitting the target(melee)/shooting(range)
- - Explosive moves can cause an explosion.
- - HP draining moves can heal your Pokemon from the damage they dealt.
- - If a special attack move is a contact move,the Pokemon will use the melee instead of shooting projectiles.However the damage still uses the Special Attack stat.(e.g. Draining Kiss) 
- - Most of the physical ball and bomb moves shoot a projectile instead of melee,it still uses the Attack stat to calculate the damage.(Both of the Ice Ball and Magnet Bomb are  ball and bomb moves.But I don't think Ice Ball shoots anything according to its description,while Magnet Bomb truly shoots a bomb.So using Ice Ball don't shoot anything.)
- **Mobs killed by your pokemon will drop items and experience like it was killed by a tamed wolf.**
- Your pokemon can gain experience and ev by killing pokemon without starting a pokemon battle(needs to be **the last mob** that deals the damage,can be disabled in the config)
- Your pokemon can evolve by using the move to hit other mobs instead of starting a Pokemon Battle(e.g. Primeape needs to use Rage Fist 20 times to evolve,now you can do it without a traditional Pokemon Battle)(Configurable).
- Adds the Oran Lucky Egg(held item) to gain more experience from pokemon killed by your pokemon,right-click your pokemon while sneaking to give the item to the pokemon.(**The Oran Lucky Egg won't give you extra xp from any other ways!**)
- **Animation Support** Support for animations from cobblemon mod when attacking(These animations are not designed for this mod so it might be weird)
- Your pokemon can taunt the pokemon that attacks you.(Your pokemon can't be taunted.)
- - Moves(taunt,follow me,rage powder,torment) are needed,and they need to be set as the first move in the move set. to taunt the wild pokemon.(can be disabled in the config)
- Pokemon with higher defense stat can lower the damage it take.(configurable)
- A new hotkey to let your pokemon start a battle with the pokemon that tries to attack you.
- Some abilities(intimidate,unnerve,pressure) can lower the nearby pokemon's aggro.
- The Wimpod line Pokemon will be recalled when taking damage and the health is below 50%.
### Features that is not released currently:
* Moves like earthquake will hurt the entities nearby when the Pokemon hits the target.
* **Type effectiveness for some non-pokemon entities** These effects don't strictly follow the rules of Pokemon,instead,they are more closely related to minecraft itself:
* * Water type damage will be more effective on mobs that takes damage from the water.
* * Fire type damage will be not very effective against fire immune entities(x0.1 by default,set to 0 might be confusing for new player).
* * Ice type damage will be not very effective against entities that don't take the frost damage(still x0.1).
* * Ice type damage will be more effective against entities that has a weaker resistant to the frost damage,(blaze,magma cube,etc.).
* * Poison type damage will be not very effective against undead mobs.(x0.1 again)
## TODO
- Type effectiveness for Pokemon entities:There doesn't seem to be a built-in type modifier list in Cobblemon,the data might be sent to showdown to process,which means I should spend some time to add it myself and test it.
- Give more special effects to different moves.
- Special effect for status moves. (Most of the status moves has no effect currently,they can be used as a way to make your pokemon passive.)
- Special effect for abilities like aftermath,bulletproof,soundproof,etc.
- More config options for fight of flight choices.
- The pp of the pokemon moves will be consumed after using it outside the battle(this feature could make the pokemon obviously weaker at the early game and the moves don't need to be balanced that way currently ,so I won't work on it until the mod got cool enough.)
## Known Issues
- Damage of moves that needs the stat like electro ball can't be calculated properly.(I don't know how to find a proper way to calculate the damage if this move is used to attack a non-Pokemon mob,so I won't fix it until I figure out a way.Sorry about that.)
## How to use the Poke Staff
1. Get one Poke Staff by crafting or get it in the creative mode.
2. Sneak+Right click: Switch the mode of the staff  
Right click:Send the command to the pokemon(send command mode)/Select the move(select mode)