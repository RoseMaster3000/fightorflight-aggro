package me.rufia.fightorflight.item;


import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface ItemFightOrFlight {
    DeferredRegister<Item> ITEMS=DeferredRegister.create(CobblemonFightOrFlight.MODID, Registries.ITEM);
    RegistrySupplier<Item> POKESTAFF=register("pokestaff",()->new PokeStaff(new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES)));
    RegistrySupplier<Item> ORANLUCKYEGG=register("oran_lucky_egg",()->new OranLuckyEgg(new Item.Properties().stacksTo(64).arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES)));
    static RegistrySupplier<Item> register(String name, Supplier<Item> item) {
        return ITEMS.register(new ResourceLocation(CobblemonFightOrFlight.MODID, name), item);
    }
    static void bootstrap(){
        ITEMS.register();
    }

}
