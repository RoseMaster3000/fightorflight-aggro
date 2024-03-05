# What this branch do?
- In this branch, I made some changes to the original Fight or Flight mod to make it more interesting.  
- This branch is still WIP and it's my first time to participate in the development of a mod so something may not go smoothly as I had expected. This mod still needs to be polished before releasing.
## Features&Changes
- **Lower Pokemon Damage:** I noticed that some players commented on the curseforge page that the pokemon damage was too high ,so I lowered the default value of the maximum damage.
- **Configurable aggresion:** Added a multiplier so that you can multiply the level of the pokemon when calculating its aggresion.
- **Faster Pokemon:** Added a multiplier so the Pokemon with a higher speed stat can run faster.(can be changed in the config)
- **Range attack!:** Added a range attack for pokemon whose Sp.ATK is higher than its ATK.
- - Wild pokemon are not allowed to use the range attack.(can be enabled in the config)
- **Different ways of range attack:** If a pokemon has some special moves,they will shoot different bullet.
- - The moves' type and power will influence the projectile's if the moves is a special move.However, if your pokemon doesn't have these moves there is also a base value(60 by default) .
- - You can use the Poke Staff to select the move you want to choose, even forcing a special attacker to melee!(use JEI to check the recipe)
- **Special effect for moves** 
- - The pokemon can now teleport to a nearby position if it learns the teleport move and got panicked.(can be disabled in the config)
- - Player's pokemon will be recalled automatically when using moves like u-turn and hit the target(melee)/shoot(range)
## TODO
- Well, I do need some time to learn java.
- Give more special effect to different moves.
- Special effect for status moves. 
- The pp of the pokemon moves will be consumed after using it outside the battle(this feature could make the pokemon  weak at the early game and the moves don't need to be balanced that way currently ,so I won't work on it until the mod got cool enough)  
## Known Issues
- Moves like electro ball can't calculate the damage correctly.  

## How to use the Poke Staff
Sneak+Right click: Switch the mode of the staff
Right click:Send the command to the pokemon(send command mode)/Select the move(select mode)