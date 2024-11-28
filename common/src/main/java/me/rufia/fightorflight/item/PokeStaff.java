package me.rufia.fightorflight.item;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
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
        SEND, SETMOVE, SETCMDMODE
    }

    enum CMDMODE {
        MOVE_ATTACK, MOVE, STAY, NOCMD
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
                int d = getMoveSlot(stack);
                String cmdMode = getCommandMode(stack);
                Component component;
                switch (MODE.valueOf(modeTag)) {
                    case SEND -> component = Component.translatable("item.fightorflight.pokestaff.mode.send");
                    case SETMOVE ->
                            component = Component.translatable("item.fightorflight.pokestaff.mode.selectmoveslot");
                    case SETCMDMODE ->
                            component = Component.translatable("item.fightorflight.pokestaff.mode.selectcommand");
                    default -> component = Component.literal("");
                }

                tooltipComponents.add(Component.translatable("item.fightorflight.pokestaff.desc1").append(component));
                tooltipComponents.add(Component.translatable("item.fightorflight.pokestaff.desc2", d + 1));
                tooltipComponents.add(Component.translatable("item.fightorflight.pokestaff.desc3",getTranslatedCmdModeName(cmdMode).getString()));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        CompoundTag tag = stack.getOrCreateTag();
        initTag(stack);

        if (player.isSecondaryUseActive()) {
            //CobblemonFightOrFlight.LOGGER.info("SNEAKING");
            CompoundTag tag2 = tag.getCompound("command");
            switch (MODE.valueOf(tag2.getString("mode"))) {
                case SETMOVE -> {
                    tag2.putString("mode", MODE.SETCMDMODE.name());
                    if (player.level().isClientSide) {
                        player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.mode.selectcommand"));
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
                case SETCMDMODE -> {
                    tag2.putString("mode", MODE.SEND.name());
                    if (player.level().isClientSide) {
                        player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.mode.send"));
                    }
                }
            }
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }

        if (MODE.valueOf(tag.getCompound("command").getString("mode")) == MODE.SETMOVE) {
            //CobblemonFightOrFlight.LOGGER.info("SELECTING MOVES");
            int nextMoveSlot = getMoveSlot(stack) + 1;
            setMoveSlot(stack, nextMoveSlot, player);
            setCommandMode(stack, CMDMODE.NOCMD.name());
            if (player.level().isClientSide) {
                player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.desc2", nextMoveSlot % 4 + 1));
            }
        }
        if (MODE.valueOf(tag.getCompound("command").getString("mode")) == MODE.SETCMDMODE) {
            commandModeSelectNext(stack, getCommandMode(stack));
            setMoveSlot(stack, -1, player);
            if(player.level().isClientSide){
                player.sendSystemMessage(getTranslatedCmdModeName(getCommandMode(stack)));
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        initTag(stack);
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
        if (!livingEntity.level().isClientSide && livingEntity instanceof PokemonEntity pokemonEntity) {
            if (pokemonEntity.getOwner() == player) {
                ItemStack heldItem = pokemonEntity.getPokemon().heldItem();
                if (heldItem.is(CobblemonItems.CHOICE_SCARF) || heldItem.is(CobblemonItems.CHOICE_BAND) || heldItem.is(CobblemonItems.CHOICE_SPECS)) {
                    return;
                }
                CompoundTag tag = itemStack.getOrCreateTag();
                if (tag.contains("command") && itemStack.is(ItemFightOrFlight.POKESTAFF.get())) {
                    int moveSlot = getMoveSlot(itemStack);
                    String cmdMode = getCommandMode(itemStack);
                    if (moveSlot != -1) {
                        Move move = pokemonEntity.getPokemon().getMoveSet().get(moveSlot);
                        if (move == null) {
                            move = pokemonEntity.getPokemon().getMoveSet().get(0);
                        }
                        ((PokemonInterface) (Object) pokemonEntity).setCurrentMove(move);
                        player.sendSystemMessage(Component.translatable("item.fightorflight.pokestaff.send.result.move").append(move.getDisplayName()));
                        //CobblemonFightOrFlight.LOGGER.info(moveSlot + pokemonEntity.getPokemon().getMoveSet().get(moveSlot).getName());
                    } else if (cmdMode != CMDMODE.NOCMD.name()) {

                    }
                }
            }
        }
    }

    private void initTag(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains("command")) {
            CompoundTag tag2 = itemStack.getOrCreateTagElement("command");
            tag2.putString("mode", MODE.SETMOVE.name());
            tag2.putInt("move_slot", 0);
            tag2.putString("command_mode", CMDMODE.NOCMD.name());
            tag.put("commnad", tag2);
        }
    }

    public int getMoveSlot(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (itemStack.is(ItemFightOrFlight.POKESTAFF.get())) {
            if (tag.contains("command")) {
                return tag.getCompound("command").getInt("move_slot");
            }
        }
        return -1;
    }

    public String getCommandMode(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (itemStack.is(ItemFightOrFlight.POKESTAFF.get())) {
            if (tag.contains("command")) {
                return tag.getCompound("command").getString("command_mode");
            }
        }
        return CMDMODE.NOCMD.name();
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

    protected void setMoveSlot(ItemStack stack, int moveSlot, Player player) {
        stack.getOrCreateTag().getCompound("command").putInt("move_slot", moveSlot % 4);
    }

    protected void setCommandMode(ItemStack stack, String mode) {
        stack.getOrCreateTag().getCompound("command").putString("command_mode", mode);
    }

    protected void commandModeSelectNext(ItemStack stack, String mode) {
        String cmd;
        switch (CMDMODE.valueOf(mode)) {
            case MOVE_ATTACK -> cmd = CMDMODE.MOVE.name();
            case MOVE -> cmd = CMDMODE.STAY.name();
            case STAY -> cmd = CMDMODE.NOCMD.name();
            case NOCMD -> cmd = CMDMODE.MOVE_ATTACK.name();
            default -> cmd = CMDMODE.NOCMD.name();
        }
        setCommandMode(stack, cmd);
    }

    protected Component getTranslatedCmdModeName(String cmdModeName){
        Component component;
        switch (CMDMODE.valueOf(cmdModeName)){
            case MOVE_ATTACK -> component=Component.translatable("item.fightorflight.pokestaff.command.move_attack");
            case MOVE -> component=Component.translatable("item.fightorflight.pokestaff.command.move");
            case STAY -> component=Component.translatable("item.fightorflight.pokestaff.command.stay");
            case NOCMD -> component=Component.translatable("item.fightorflight.pokestaff.command.no_cmd");
            default -> component=null;
        }
        return component;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
