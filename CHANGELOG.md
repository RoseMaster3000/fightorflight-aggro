## v0.6.1
* **Animation Support** Support for animations from cobblemon mod when attacking(These animations are not designed for this mod so it might be weird)
* Wild Pokemon cries correctly when provoked.
* Player's Pokemon can taunt wild Pokemon.
* Added a new hotkey that let your pokemon start a battle with the pokemon that tries to attack you.
* Some abilities(intimidate, unnerve, pressure) can lower the nearby pokemon's aggro.
* The Wimpod line Pokemon will be recalled when taking damage and the health is below 50%.
* Using move outside battle can be used to evolve a Pokemon like Annihilape.
* Pokemon aiming optimization, increasing the accuracy.
* More specific move classification.
* The projectiles of ball and bomb moves can cause a small explosion that don't break the blocks.
* Balance tweaks.
* Bug fixes.
## v0.6.0
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