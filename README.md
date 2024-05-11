# What this branch do?
- In this branch, I made some changes to the original Fight or Flight mod to make it more interesting.  
- This branch is still WIP, and it's my first time to participate in the mod development so something may not go smoothly as I had expected. 
- Well,I had used this mod in my save for hours,it won't crash your game at least.
## Features&Changes
- **Lower Pokemon Damage:** I noticed that some players commented on the curseforge page that the pokemon damage was too high ,so I lowered the default value of the maximum damage.
- **Configurable aggresion:** Added a multiplier so that you can multiply the level of the pokemon when calculating its aggresion.
- **Faster Pokemon:** Pokemon with a higher speed stat can run faster.(can be changed in the config)
- **Range attack!:** Added a range attack for pokemon whose Sp.ATK is higher than its ATK.
- - Wild pokemon are not allowed to use the range attack.(can be enabled in the config)
- **Different ways of range attack:** If a pokemon has some special moves,they will shoot different bullet.
- - The moves' type and power will influence the projectile's if the moves is a special move.However, if your pokemon doesn't have these moves, the type of the projectile will be based on the pokemon's primary type and the power will be set to 60(can be changed in the config).
- - You can use the Poke Staff to select the move you want to use, even forcing a special attacker to melee!(use JEI to check the recipe)
- **Special effect for moves** 
- - The panicked pokemon can teleport to a nearby position if it learns the teleport move.(can be disabled in the config)
- - Player's pokemon will be recalled automatically when using moves like U-turn and hitting the target(melee)/shooting(range)
- - Explosive moves can cause an explosion.
- **Mobs killed by your pokemon will drop items and experience like it was killed by a tamed wolf.**
- Your pokemon can gain experience and ev by killing pokemon without starting a pokemon battle(needs to be **the last mob** that deals the damage,can be disabled in the config)
- Adds the Oran Lucky Egg(held item) to gain more experience from pokemon killed by your pokemon,right-click your pokemon while sneaking to give the item to the pokemon.(**The Oran Lucky Egg won't give you extra xp from any other ways!**)
## TODO
- Give more special effects to different moves.
- Special effect for status moves. 
- Special effect for abilities like aftermath and emergency exit.
- The pp of the pokemon moves will be consumed after using it outside the battle(this feature could make the pokemon  weak at the early game and the moves don't need to be balanced that way currently ,so I won't work on it until the mod got cool enough)
## Known Issues
- Damage of moves like electro ball can't be calculated properly.  
- Small pokemon like mawile is hard to be aimed at by the pokemon's projectiles(the projectile flies over the pokemon's head.)
## How to use the Poke Staff
1. Get one Poke Staff by crafting or get it in the creative mode.
2. Sneak+Right click: Switch the mode of the staff  
Right click:Send the command to the pokemon(send command mode)/Select the move(select mode)