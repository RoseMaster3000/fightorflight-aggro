package me.rufia.fightorflight.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.server.BattleChallengePacket;
import com.cobblemon.mod.common.net.messages.server.RequestPlayerInteractionsPacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.client.keybinds.KeybindFightOrFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftClientInject {
    @Shadow
    public static Minecraft getInstance() {
        return null;
    }

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "tick", at = @At("TAIL"))
    private void postTick(CallbackInfo ci) {
        if (KeybindFightOrFlight.START_BATTLE.isDown()) {
            startBattle();
        }
    }

    private void startBattle() {
        var player = getInstance().player;
        boolean isSpectator = player.isSpectator();
        boolean playerIsNotAvailable = CobblemonClient.INSTANCE.getBattle() != null;
        boolean otherConditions = !(CobblemonClient.INSTANCE.getStorage().getSelectedSlot() != -1 && getInstance().screen == null);
        if (isSpectator || playerIsNotAvailable || otherConditions) {
            return;
        }

        Pokemon pokemon = CobblemonClient.INSTANCE.getStorage().getMyParty().get(CobblemonClient.INSTANCE.getStorage().getSelectedSlot());
        if (pokemon != null && pokemon.getCurrentHealth() > 0) {
            var entities = player.clientLevel.getEntitiesOfClass(PokemonEntity.class, AABB.ofSize(player.getPosition(player.tickCount), 16, 16, 16),
                    (pokemonEntity) -> pokemonEntity.getTarget() == player
            );
            for (PokemonEntity pokemonEntity : entities) {
                if (pokemonEntity.getOwner() == null && pokemonEntity.canBattle(player)) {
                    BattleChallengePacket packet=new BattleChallengePacket(pokemonEntity.getId(), pokemon.getUuid(), BattleFormat.Companion.getGEN_9_SINGLES());
                    //packet.sendToServer();
                    CobblemonNetwork.INSTANCE.sendToServer(packet);
                    //CobblemonFightOrFlight.LOGGER.info("sending battle packet");
                    //player.sendSystemMessage(Component.literal("Sending battle packet - message"));
                    break;
                } else if (pokemonEntity.getOwner() != player) {
                    if (pokemonEntity.getOwner() instanceof Player) {
                        CobblemonNetwork.INSTANCE.sendToServer(new RequestPlayerInteractionsPacket(pokemonEntity.getUUID(), pokemonEntity.getId(), pokemon.getUuid()));
                        //break;
                    }
                } else {
                    //CobblemonFightOrFlight.LOGGER.info("You must be joking");
                }
            }
        }
    }
}
