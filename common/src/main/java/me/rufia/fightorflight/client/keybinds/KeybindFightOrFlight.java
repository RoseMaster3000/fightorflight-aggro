package me.rufia.fightorflight.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class KeybindFightOrFlight {
    public static List<KeyMapping> bindings=new ArrayList<>();
    public static KeyMapping START_BATTLE;
    static {
       START_BATTLE= new KeyMapping("key.fightorflight.startbattle", InputConstants.Type.KEYSYM, InputConstants.KEY_G, KeybindCategories.FOF);
       bindings.add(START_BATTLE);
    }
}
