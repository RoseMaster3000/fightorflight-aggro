package me.rufia.fightorflight.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.server.BattleChallengePacket;
import com.cobblemon.mod.common.net.messages.server.RequestPlayerInteractionsPacket;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.client.keybinds.KeybindFightOrFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
        boolean isSpectator=player.isSpectator();
        boolean playerIsAvailable=CobblemonClient.INSTANCE.getBattle() != null;
        boolean otherConditions=!(CobblemonClient.INSTANCE.getStorage().getSelectedSlot() != -1 && getInstance().screen == null);
        if (isSpectator||playerIsAvailable||otherConditions) {
            return;
        }

        var pokemon = CobblemonClient.INSTANCE.getStorage().getMyParty().get(CobblemonClient.INSTANCE.getStorage().getSelectedSlot());
        if (pokemon != null && pokemon.getHp() > 0) {
            var entities = player.clientLevel.getEntitiesOfClass(PokemonEntity.class, AABB.ofSize(player.getPosition(player.tickCount), 20, 20, 20),
                    (pokemonEntity) -> pokemonEntity.getTarget() == player
            );
            for (PokemonEntity pokemonEntity : entities) {
                if (pokemonEntity.getOwner() == null && pokemonEntity.canBattle(player)) {
                    CobblemonNetwork.INSTANCE.sendToServer(new BattleChallengePacket(pokemonEntity.getId(), pokemon.getUuid(), BattleFormat.Companion.getGEN_9_SINGLES()));
                    break;
                } else if (pokemonEntity.getOwner() != player) {
                    if (pokemonEntity.getOwner() instanceof Player player1) {
                        CobblemonNetwork.INSTANCE.sendToServer(new RequestPlayerInteractionsPacket(pokemonEntity.getUUID(), pokemonEntity.getId(), pokemon.getUuid()));
                        break;
                    }
                } else {
                    CobblemonFightOrFlight.LOGGER.info("Oh gosh,you must be joking");
                }
            }
        }
    }
}
