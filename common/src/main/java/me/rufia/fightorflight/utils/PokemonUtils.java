package me.rufia.fightorflight.utils;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.animation.PlayPoseableAnimationPacket;
import com.cobblemon.mod.common.net.messages.client.effect.RunPosableMoLangPacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.evolution.progress.UseMoveEvolutionProgress;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Set;

public class PokemonUtils {
    public static boolean shouldMelee(PokemonEntity pokemonEntity) {
        Move move = getMeleeMove(pokemonEntity);
        boolean b1 = pokemonEntity.getPokemon().getAttack() > pokemonEntity.getPokemon().getSpecialAttack();//The default setting.
        boolean b2 = pokemonEntity.getOwner() == null;//The pokemon has no trainer.
        boolean b3 = move != null;//The trainer selected a physical move.
        if (b2) {
            return b1 || !CobblemonFightOrFlight.commonConfig().wild_pokemon_ranged_attack;//wild pokemon choose the strongest way to attack
        } else {
            return b3;
        }
    }

    public static boolean shouldShoot(PokemonEntity pokemonEntity) {
        Move move = getRangeAttackMove(pokemonEntity);
        boolean b1 = pokemonEntity.getPokemon().getAttack() < pokemonEntity.getPokemon().getSpecialAttack();//The default setting.
        boolean b2 = pokemonEntity.getOwner() == null;//The pokemon has no trainer.
        boolean b3 = move != null;//The trainer selected a physical move.
        if (b2) {
            return b1 && CobblemonFightOrFlight.commonConfig().wild_pokemon_ranged_attack;//wild pokemon choose the strongest way to attack
        } else {
            return b3;
        }
    }

    public static Move getMove(PokemonEntity pokemonEntity) {
        if (pokemonEntity == null) {
            CobblemonFightOrFlight.LOGGER.info("PokemonEntity is null");//This will be shown if the projectile hits the target and the pokemon is recalled
            return null;
        }
        String moveName = !(((PokemonInterface) (Object) pokemonEntity).getCurrentMove() == null) ? (((PokemonInterface) (Object) pokemonEntity).getCurrentMove()) : pokemonEntity.getPokemon().getMoveSet().get(0).getName();
        Move move = null;
        boolean flag = false;
        if (moveName == null) {
            return null;
        }
        for (MoveTemplate m : pokemonEntity.getPokemon().getAllAccessibleMoves()) {
            move = m.create();
            if (m.getName().equals(moveName)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            move = pokemonEntity.getPokemon().getMoveSet().get(0);
        }
        if (move == null) {
            CobblemonFightOrFlight.LOGGER.warn("Returning a null move for no reason");
        }
        return move;
    }

    @Deprecated
    public static Move getMove(PokemonEntity pokemonEntity, boolean getSpecial) {
        Move move = getMove(pokemonEntity);
        if (move == null) {
            return null;
        }
        boolean isSpecial = move.getDamageCategory() == DamageCategories.INSTANCE.getSPECIAL();
        boolean isPhysical = move.getDamageCategory() == DamageCategories.INSTANCE.getPHYSICAL();

        if ((isSpecial && getSpecial) || (isPhysical && !getSpecial)) {
            ((PokemonInterface) pokemonEntity).setCurrentMove(move);
            return move;
        }
        return null;
    }

    public static boolean isMeleeAttackMove(Move move) {
        if (move == null) {
            return true;
        }
        String moveName = move.getName();
        boolean isSpecial = move.getDamageCategory() == DamageCategories.INSTANCE.getSPECIAL();
        boolean isPhysical = move.getDamageCategory() == DamageCategories.INSTANCE.getPHYSICAL();
        boolean b1 = isPhysical && !(Arrays.stream(CobblemonFightOrFlight.moveConfig().single_bullet_moves).toList().contains(moveName) || Arrays.stream(CobblemonFightOrFlight.moveConfig().physical_single_arrow_moves).toList().contains(moveName));
        boolean b2 = isSpecial && (Arrays.stream(CobblemonFightOrFlight.moveConfig().special_contact_moves).toList().contains(moveName));
        return b1 || b2;
    }

    public static Move getMeleeMove(PokemonEntity pokemonEntity) {
        Move move = getMove(pokemonEntity);
        if (move == null) {
            return null;
        }

        if (isMeleeAttackMove(move)) {
            ((PokemonInterface) pokemonEntity).setCurrentMove(move);
            return move;
        }
        return null;
    }

    public static Move getRangeAttackMove(PokemonEntity pokemonEntity) {
        Move move = getMove(pokemonEntity);
        if (move == null) {
            return null;
        }
        if (!isMeleeAttackMove(move)) {
            ((PokemonInterface) pokemonEntity).setCurrentMove(move);
            return move;
        }
        return null;
    }

    public static void makeParticle(int particleAmount, Entity entity, SimpleParticleType particleType) {
        Level level = entity.level();
        if (particleAmount > 0) {
            double d = 0;
            double e = 0;
            double f = 0;
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(particleType, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), particleAmount, d, e, f, 1f);
            } else {
                for (int j = 0; j < particleAmount; ++j) {
                    level.addParticle(particleType, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), d, e, f);
                }
            }
        }
    }

    public static void setHurtByPlayer(PokemonEntity pokemonEntity, Entity target) {
        Entity owner = pokemonEntity.getOwner();
        if (owner instanceof Player player) {
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.setLastHurtByPlayer(player);
                //CobblemonFightOrFlight.LOGGER.info("Hurt by player's cobblemon");
            }
        }
    }

    public static boolean canTaunt(PokemonEntity pokemonEntity) {
        if (!CobblemonFightOrFlight.moveConfig().taunt_moves_needed) {
            return true;
        }
        boolean result = false;
        var moveSet = pokemonEntity.getPokemon().getMoveSet();
        for (Move move : moveSet) {
            if (Arrays.stream(CobblemonFightOrFlight.moveConfig().taunting_moves).toList().contains(move.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean isExplosiveMove(String moveName) {
        return Arrays.stream(CobblemonFightOrFlight.moveConfig().explosive_moves).toList().contains(moveName);
    }

    public static void createSonicBoomParticle(PokemonEntity pokemonEntity, LivingEntity target) {
        if (target == null) {
            return;
        }
        float height = pokemonEntity.getEyeHeight();
        Vec3 vec1 = pokemonEntity.position().add(0, height, 0);
        Vec3 vec2 = target.getEyePosition().subtract(vec1);
        Vec3 vec3 = vec2.normalize();
        for (int i = 1; i < Mth.floor(vec2.length()) + 1; ++i) {
            Vec3 vec4 = vec1.add(vec3.scale(i));
            Level level = target.level();
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, vec4.x, vec4.y, vec4.z, 1, 0, 0, 0, 0);
            }
        }
    }


    public static void sendAnimationPacket(PokemonEntity pokemonEntity, String mode) {
        if (!((LivingEntity) pokemonEntity).level().isClientSide) {
            var pkt = new PlayPoseableAnimationPacket(pokemonEntity.getId(), Set.of(mode), Set.of());
            pokemonEntity.level().getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(pokemonEntity.position(), 64.0, 64.0, 64.0), (livingEntity) -> true).forEach((pkt::sendToPlayer));
        }
    }

    public static void updateMoveEvolutionProgress(Pokemon pokemon, MoveTemplate move) {
        if (UseMoveEvolutionProgress.Companion.supports(pokemon, move) && CobblemonFightOrFlight.commonConfig().can_progress_use_move_evoluiton) {
            UseMoveEvolutionProgress progress = pokemon.getEvolutionProxy().current().progressFirstOrCreate(evolutionProgress -> {
                        if (evolutionProgress instanceof UseMoveEvolutionProgress umep) {
                            return umep.currentProgress().getMove().equals(move);
                        }
                        return false;
                    }
                    , UseMoveEvolutionProgress::new);
            progress.updateProgress(new UseMoveEvolutionProgress.Progress(move, progress.currentProgress().getAmount() + 1));
        }
    }

    public static boolean shouldRetreat(PokemonEntity pokemonEntity) {
        ItemStack i = pokemonEntity.getPokemon().heldItem();
        return pokemonEntity.getHealth() < pokemonEntity.getMaxHealth() * 0.5 && Arrays.stream(CobblemonFightOrFlight.moveConfig().emergency_exit_like_abilities).toList().contains(pokemonEntity.getPokemon().getAbility().getName());
    }

    public static void makeCobblemonParticle(Entity entity, String particleName) {
        if (entity != null) {
            var packet = new RunPosableMoLangPacket(entity.getId(), Set.of(String.format("q.particle('cobblemon:%s', 'target')", particleName)));
            packet.sendToPlayersAround(entity.getX(), entity.getY(), entity.getZ(), 50, entity.level().dimension(), (serverPlayer) -> false);
        }
        //todo I still need to find a way to update the locator or the particle can't be spawned at the target's location.
    }

    public static ItemStack getHeldItem(PokemonEntity pokemonEntity) {
        if (pokemonEntity == null) {
            return null;
        }
        return getHeldItem(pokemonEntity.getPokemon());
    }

    public static ItemStack getHeldItem(Pokemon pokemon) {
        if (pokemon == null) {
            return null;
        }
        return pokemon.heldItem();
    }

    public static boolean isUsingNewHealthMechanic() {
        return CobblemonFightOrFlight.commonConfig().shouldOverrideUpdateMaxHealth;
    }

    public static int getMaxHealth(PokemonEntity pokemonEntity) {
        return getMaxHealth(pokemonEntity.getPokemon());
    }

    public static int getHPStat(Pokemon pokemon) {
        return pokemon.getHp();//TODO don't forget to replace this one,this will be deprecated
    }

    public static int getMaxHealth(Pokemon pokemon) {
        int hpStat = getHPStat(pokemon);
        int minStat = CobblemonFightOrFlight.commonConfig().min_HP_required_stat;
        int midStat = CobblemonFightOrFlight.commonConfig().mid_HP_required_stat;
        int maxStat = CobblemonFightOrFlight.commonConfig().max_HP_required_stat;
        int stat = Mth.clamp(hpStat, minStat, maxStat);
        int minHealth = CobblemonFightOrFlight.commonConfig().min_HP;
        int midHealth = CobblemonFightOrFlight.commonConfig().mid_HP;
        int maxHealth = CobblemonFightOrFlight.commonConfig().max_HP;
        int health = minHealth;
        health = Math.round(
                stat < midStat ?
                        Mth.lerp((float) (stat - minStat) / (midStat - minStat), minHealth, midHealth) :
                        Mth.lerp((float) (stat - midStat) / (maxStat - midStat), midHealth, maxHealth));
        return health;//The return value is a mathematical integer,but some calculation needs a float.
    }

    public static void entityHpToPokemonHp(PokemonEntity pokemonEntity, float amount, boolean isHealing) {
        Pokemon pokemon = pokemonEntity.getPokemon();
        if (pokemon.getCurrentHealth() == 0) {
            return;
        }
        float ratio = amount / getMaxHealth(pokemonEntity);
        int val = pokemon.getCurrentHealth() + (int) Math.floor(ratio * getHPStat(pokemon)) * (isHealing ? 1 : -1);
        pokemon.setCurrentHealth(val);
    }
}
