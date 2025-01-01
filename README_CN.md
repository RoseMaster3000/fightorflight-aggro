## 没有在服务端经过测试，请自行承担可能的风险
### 施工中
### 需要[Architectury](https://modrinth.com/mod/architectury-api) !!!
## 自 0.5.3 （原mod最后一个版本） 之后的特性/改动
### v0.7.0 已经实现的特性/改动:
- **关闭野生宝可梦主动攻击** 我个人不喜欢这个特性，宝可梦无时无刻不在刷，尽管不是所有宝可梦都会主动攻击，但是当主动攻击开启时你很容易就会获得在朱/紫被肯泰罗群殴的糟糕的游戏体验。 当然你要想开可以在config中打开
- **更低的最高伤害值:** 我看到有人在curseforge页面上说宝可梦伤害太高了，所以我把最高伤害的默认值调低了。作为补偿，达到最高伤害所需的能力值也被调低了。你也可以使用config进一步调整
- **Configurable aggresion:** Added a multiplier so that you can multiply the level of the pokemon when calculating its aggresion.
- **Faster Pokemon:** Pokemon with a higher speed stat can run faster.(can be changed in the config, the base speed of cobblemon is too slow, so it might not be obvious.)
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
- - If a special attack move is a contact move,the Pokemon will use the melee instead of shooting projectiles.However the damage calculation still uses the Special Attack stat.(e.g. Draining Kiss) 
- - Most of the physical ball and bomb moves shoot a projectile instead of melee,it still uses the Attack stat to calculate the damage.(Both of the Ice Ball and Magnet Bomb are  ball and bomb moves.But I don't think Ice Ball shoots anything according to its description,while Magnet Bomb truly shoots a bomb.So using Ice Ball don't shoot anything.)
- **Mobs killed by your pokemon will drop items and experience like it was killed by a tamed wolf.**
- Your pokemon can gain experience and ev by killing pokemon without starting a pokemon battle(needs to be **the last mob** that deals the damage,can be disabled in the config)
- Your pokemon can evolve by using the move to hit other mobs instead of starting a Pokemon Battle(e.g. Primeape needs to use Rage Fist 20 times to evolve,now you can do it without a traditional Pokemon Battle)(Configurable).
- Adds the Oran Lucky Egg(held item) to gain more experience from pokemon killed by your pokemon,right-click your pokemon while sneaking to give the item to the pokemon.(**The Oran Lucky Egg won't give you extra xp from any other ways!**)
- **动画支持** 支持Cobblemon的技能动画(这些动画不是为了这个mod设计的，看着怪很正常)
- 你的宝可梦可以嘲讽攻击你的宝可梦(玩家的宝可梦不吃嘲讽)
- - 需要特殊技能(挑衅,看我嘛,愤怒粉,无理取闹) (可以在config禁用)
- Pokemon with higher defense stat can lower the damage it take.(configurable)
- A new hotkey to let your pokemon start a battle with the pokemon that tries to attack you.（not working currently)
- Some abilities(intimidate,unnerve,pressure) can lower the nearby pokemon's aggro.
- 具甲武者一家血量在一半以下时受到伤害会免疫这次伤害并被收回。
* 地震等aoe在击中目标时会伤害附近的实体.(会导致特殊攻击变成近战)
* **非宝可梦实体的属性克制** 这些效果并不严格遵守宝可梦的规则，相反，它们更加接近Minecraft本身：
* * 水系伤害对接触水受伤的生物造成更多伤害
* * 火系伤害对免疫火系的生物伤害降低(默认x0.1，设置成0可能令新玩家困惑).
* * 冰系伤害对不接受冰冻伤害的生物伤害降低(还是 x0.1).
* * 冰系伤害对更容易收到冰冻伤害的生物更加有效(烈焰人、岩浆怪等).
* * 毒系伤害对亡灵生物伤害降低.(依然是x0.1)
* 血量机制重做
* * 用了mixin来“替换”Cobblemon自己基于种族值而不是能力值的实体血量计算，如果你不喜欢我的改动的话将config/fightorflight.json5里的shouldOverrideUpdateMaxHealth设置为false来禁用它.
* * 宝可梦实体的血量不再由种族值决定而是由能力值决定。
* * 对宝可梦实体造成伤害会导致宝可梦血量下降，治疗实体也会治疗宝可梦本身(建议使用 [Healing Campfire](https://modrinth.com/mod/healing-campfire)). (这个特性用到了前文提到的计算血量最大值的机制，禁用它会同时禁用本机制)
* * 注意：用道具治疗宝可梦本身不会给实体回血。请先收回宝可梦，喂回血道具，再放出来。
* 部分携带道具可以提供伤害增益(属性增强道具,讲究眼镜,讲究头带,力量头带,博识眼镜,生命宝珠)
* * 生命宝珠会像游戏里一样对宝可梦自身造成伤害.强行和魔法防守的宝可梦免疫这个伤害.
* 不同于主系列，强行会增强所有招式，但不会再有附加状态效果等特效（比如火系技能不会再让生物着火。
* 弹类弹射物会产生一个小爆炸，火系技能的爆炸会点燃地面(我自己不喜欢，默认关掉了，你要在config里手动启用。骚瑞。)
### Features that is not released currently(will be released after I fix the bug):
- Nope. I will be busy for a few days so the development should slow down.
## TODO
- Attack Position for Poke Staff(not available currently. It is quite difficult.)
- Give more special effects to different moves.
- Special effect for status moves. (Most of the status moves has no effect currently,they can be used as a way to make your pokemon passive.)
- Special effect for abilities like aftermath,bulletproof,soundproof,etc.
- More config options for fight of flight choices.
- Type effectiveness for Pokemon entities:There doesn't seem to be a built-in type modifier list in Cobblemon,the data might be sent to showdown to process,which means I should spend some time to add it myself and test it.
- Use the berry/heal items that heal the pokemon to heal the Pokemon entity(The healing items don't share a unique base class, they just extends PokemonSelectingItem(the base class for many items that can open the party menu and select a Pokemon to use). I don't think adding the mixin to every class and implements an interface is a good idea.)
- The pp of the pokemon moves will be consumed after using it outside the battle(this feature could make the pokemon obviously weaker at the early game and the moves don't need to be balanced that way currently, so I won't work on it until the mod got cool enough.)
## Known Issues
- 电球这类需要双方能力值计算的技能威力无法正常计算(我目前还不确认怎么处理对非宝可梦实体使用这些招式的伤害计算所以直接一刀切了)
- 爆炸伤害的计算可能没有按照招式类型计算
### Known issues for 1.21.1 version(not released yet)
- 我原本加的快捷键不管用，目前尚未弄清楚原因。
## 如何使用宝可杖
1. 合成一个宝可杖/创造拿.
2. 潜行右键切换模式  
右键:给宝可梦发送指令/选择技能槽（此时尚未确认，需要发送指令）/选择命令