package me.rufia.fightorflight.item;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PokeStaff extends Item {
    enum MODE {
        SEND, SETMOVE
    }

    public PokeStaff(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("command")) {
            String modeTag = tag.getCompound("command").getString("mode");
            if (!modeTag.isEmpty()) {
                int d = tag.getCompound("command").getInt("data");
                Component component;
                switch (MODE.valueOf(modeTag)) {
                    case SEND -> component = Component.translatable("item.fightorflight.pokestaff.mode.send");
                    case SETMOVE ->
                            component = Component.translatable("item.fightorflight.pokestaff.mode.selectmoveslot");
                    default -> component = Component.literal("");
                }

                tooltipComponents.add(Component.translatable("item.fightorflight.pokestaff.desc1").append(component));
                tooltipComponents.add(Component.translatable("item.fightorflight.pokestaff.desc2", d + 1));
            }
        }
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("command")) {
            CompoundTag tag2 = stack.getOrCreateTagElement("command");
            tag2.putString("mode", MODE.SETMOVE.name());
            tag2.putInt("data", 0);
            tag.put("commnad", tag2);
        }

        if (player.isSecondaryUseActive()) {
            //CobblemonFightOrFlight.LOGGER.info("SNEAKING");
            CompoundTag tag2 = tag.getCompound("command");
            switch (MODE.valueOf(tag2.getString("mode"))) {
                case SETMOVE -> {
                    tag2.putString("mode", MODE.SEND.name());
                    if (player.level().isClientSide) {
                        player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.mode.send"));
                    }
                    //CobblemonFightOrFlight.LOGGER.info("sending");
                }
                case SEND -> {
                    tag2.putString("mode", MODE.SETMOVE.name());
                    if (player.level().isClientSide) {
                        player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.mode.selectmoveslot"));
                    }
                    //CobblemonFightOrFlight.LOGGER.info("SETTING Moves");
                }
            }
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        int data = tag.getCompound("command").getInt("data");
        if (MODE.valueOf(tag.getCompound("command").getString("mode")) == MODE.SETMOVE) {
            //CobblemonFightOrFlight.LOGGER.info("SELECTING MOVES");
            moveSelect(stack, data, player);
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("command")) {
            CompoundTag tag2 = stack.getOrCreateTagElement("command");
            tag2.putString("mode", MODE.SETMOVE.name());
            tag2.putInt("data", 0);
            tag.put("commnad", tag2);
        }

        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public boolean canSend(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!(tag.contains("command") && stack.is(ItemFightOrFlight.POKESTAFF.get()))) {
            return false;
        }
        return Objects.equals(tag.getCompound("command").getString("mode"), MODE.SEND.name());
    }

    public void send(Player player, LivingEntity livingEntity, ItemStack itemStack) {
        if (!livingEntity.level().isClientSide) {
            if (livingEntity instanceof PokemonEntity pokemonEntity) {
                if (pokemonEntity.getOwner() == player) {
                    ItemStack heldItem = pokemonEntity.getPokemon().heldItem();
                    if (heldItem.is(CobblemonItems.CHOICE_SCARF) || heldItem.is(CobblemonItems.CHOICE_BAND) || heldItem.is(CobblemonItems.CHOICE_SPECS)) {
                        return;
                    }
                    CompoundTag tag = itemStack.getOrCreateTag();
                    if (tag.contains("command") && itemStack.is(ItemFightOrFlight.POKESTAFF.get())) {
                        int dataTag = tag.getCompound("command").getInt("data");
                        Move move = pokemonEntity.getPokemon().getMoveSet().get(dataTag);
                        if (move == null) {
                            move = pokemonEntity.getPokemon().getMoveSet().get(0);
                        }
                        ((PokemonInterface) (Object) pokemonEntity).setCurrentMove(move);
                        player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.send.result").append(move.getDisplayName()));
                        //CobblemonFightOrFlight.LOGGER.info(dataTag + pokemonEntity.getPokemon().getMoveSet().get(dataTag).getName());
                    }
                }
            }
        }
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

    protected void moveSelect(ItemStack stack, int data, Player player) {
        stack.getOrCreateTag().getCompound("command").putInt("data", (data + 1) % 4);
        if (player.level().isClientSide) {
            player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.desc2", (data + 1) % 4 + 1));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
