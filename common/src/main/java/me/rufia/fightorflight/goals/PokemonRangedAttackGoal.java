package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import me.rufia.fightorflight.entity.projectile.AbstractPokemonProjectile;
import me.rufia.fightorflight.entity.projectile.PokemonArrow;
import me.rufia.fightorflight.entity.projectile.PokemonBullet;
import me.rufia.fightorflight.entity.projectile.PokemonTracingBullet;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;

public class PokemonRangedAttackGoal extends Goal {
    public int ticksUntilNewAngerParticle = 0;

    public int ticksUntilNewAngerCry = 0;
    private final PokemonEntity pokemonEntity;
    private final LivingEntity livingEntity;
    @Nullable
    private LivingEntity target;
    private int attackTime;
    private final double speedModifier;
    private int seeTime;

    private final float attackRadius;
    private final float attackRadiusSqr;

    public PokemonRangedAttackGoal(LivingEntity pokemonEntity, double speedModifier, float attackRadius) {
        this.attackTime = -1;
        this.livingEntity = pokemonEntity;
        if (!(pokemonEntity instanceof PokemonEntity)) {
            throw new IllegalArgumentException("PokemonRangedAttackGoal requires a PokemonEntity");
        } else {
            this.pokemonEntity = (PokemonEntity) pokemonEntity;
            this.speedModifier = speedModifier;

            this.attackRadius = attackRadius;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }
    }

    public boolean canUse() {
        if (!PokemonUtils.shouldShoot(pokemonEntity)) {
            return false;
        }

        LivingEntity livingEntity = this.pokemonEntity.getTarget();
        if (livingEntity != null && livingEntity.isAlive()) {
            this.target = livingEntity;
            return shouldFightTarget();
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.pokemonEntity.getNavigation().isDone();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean shouldFightTarget() {
        //if (FightOrFlightCommonConfigs.DO_POKEMON_ATTACK.get() == false) { return false; }
        if (pokemonEntity.getPokemon().getLevel() < CobblemonFightOrFlight.commonConfig().minimum_attack_level) {
            return false;
        }

        LivingEntity owner = pokemonEntity.getOwner();
        if (owner != null) {
            if (!CobblemonFightOrFlight.commonConfig().do_pokemon_defend_owner || (this.pokemonEntity.getTarget() == null || this.pokemonEntity.getTarget() == owner)) {
                return false;
            }

            if (this.pokemonEntity.getTarget() instanceof PokemonEntity targetPokemon) {
                LivingEntity targetOwner = targetPokemon.getOwner();
                if (targetOwner != null) {
                    if (targetOwner == owner) {
                        return false;
                    }
                    if (!CobblemonFightOrFlight.commonConfig().do_player_pokemon_attack_other_player_pokemon) {
                        return false;
                    }
                }
            }
            if (this.pokemonEntity.getTarget() instanceof Player) {
                if (!CobblemonFightOrFlight.commonConfig().do_player_pokemon_attack_other_players) {
                    return false;
                }
            }

        } else {
            if (this.pokemonEntity.getTarget() != null) {
                if (CobblemonFightOrFlight.getFightOrFlightCoefficient(pokemonEntity) <= 0) {
                    return false;
                }

                LivingEntity targetEntity = this.pokemonEntity.getTarget();
                if (this.pokemonEntity.distanceToSqr(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ()) > 400) {
                    return false;
                }
            }
        }
        //if (pokemonEntity.getPokemon().isPlayerOwned()) { return false; }

        return !pokemonEntity.isBusy();
    }

    public boolean isTargetInBattle() {
        if (this.pokemonEntity.getTarget() instanceof ServerPlayer targetAsPlayer) {
            return BattleRegistry.INSTANCE.getBattleByParticipatingPlayer(targetAsPlayer) != null;
        }
        return false;
    }

    public void tick() {
        if (!CobblemonFightOrFlight.commonConfig().do_pokemon_attack_in_battle) {
            if (isTargetInBattle()) {
                this.pokemonEntity.getNavigation().setSpeedModifier(0);
            }
        }

        if (pokemonEntity.getOwner() == null) {
            if (ticksUntilNewAngerParticle < 1) {
                CobblemonFightOrFlight.PokemonEmoteAngry((Mob) this.livingEntity);
                ticksUntilNewAngerParticle = 10;
            } else {
                ticksUntilNewAngerParticle = ticksUntilNewAngerParticle - 1;
            }

            if (ticksUntilNewAngerCry < 1) {
                pokemonEntity.cry();
                ticksUntilNewAngerCry = 100 + (int) (Math.random() * 200);
            } else {
                ticksUntilNewAngerCry = ticksUntilNewAngerCry - 1;
            }
        }
        double d = this.pokemonEntity.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean bl = this.pokemonEntity.getSensing().hasLineOfSight(this.target);
        if (bl) {
            ++this.seeTime;
        } else {
            seeTime=0;
            resetAttackTime(d);
        }

        if (!(d > (double) this.attackRadiusSqr) && this.seeTime >= 5 && bl) {
            this.pokemonEntity.getNavigation().stop();
        } else {
            this.pokemonEntity.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.pokemonEntity.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        --this.attackTime;
        ((PokemonInterface) (Object) pokemonEntity).setAttackTime(((PokemonInterface) (Object) pokemonEntity).getAttackTime() + 1);
        if (attackTime == 7 && (((PokemonInterface) (Object) pokemonEntity).usingSound())) {
            createSonicBoomParticle();
        }
        if (this.attackTime == 0) {
            if (!bl) {
                return;
            }
            resetAttackTime(d);
            this.performRangedAttack(this.target);
        } else if (this.attackTime < 0) {
            resetAttackTime(d);
        }
    }

    protected void resetAttackTime(double d) {
        ((PokemonInterface) (Object) pokemonEntity).setAttackTime(0);
        float attackSpeedModifier = Math.max(0.1f, 1 - this.pokemonEntity.getSpeed() / CobblemonFightOrFlight.commonConfig().speed_stat_limit);
        float f = (float) Math.sqrt(d) / this.attackRadius * attackSpeedModifier;
        this.attackTime = Mth.floor(20 * Mth.lerp(f, CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_interval, CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_interval));
    }

    protected void createSonicBoomParticle() {
        if (target == null) {
            return;
        }
        float height = pokemonEntity.getEyeHeight();
        Vec3 vec1 = pokemonEntity.position().add(0, height, 0);
        Vec3 vec2 = target.getEyePosition().subtract(vec1);
        Vec3 vec3 = vec2.normalize();
        for (int i = 1; i < Mth.floor(vec2.length()) + 1; ++i) {
            Vec3 vec4 = vec1.add(vec3.scale((double) i));
            Level level = target.level();
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, vec4.x, vec4.y, vec4.z, 1, 0, 0, 0, 0);
            }
        }
    }

    protected void addProjectileEntity(AbstractPokemonProjectile projectile, Move move) {
        projectile.setElementalType(move.getType().getName());
        projectile.setDamage(PokemonAttackEffect.calculatePokemonDamage(pokemonEntity, move));
        this.livingEntity.level().addFreshEntity(projectile);
    }

    protected void addProjectileEntity(AbstractPokemonProjectile projectile) {
        projectile.setElementalType(pokemonEntity.getPokemon().getPrimaryType().getName());
        projectile.setDamage(PokemonAttackEffect.calculatePokemonDamage(pokemonEntity, true));
        this.livingEntity.level().addFreshEntity(projectile);
    }

    protected void shootProjectileEntity(AbstractPokemonProjectile projectile) {
        double d = target.getX() - this.livingEntity.getX();
        double e = target.getY(0.3333333) - projectile.getY();
        double f = target.getZ() - this.livingEntity.getZ();
        float velocity = 1.6f;
        projectile.accurateShoot(d, e, f, velocity, 0.1f);

    }

    protected void performRangedAttack(LivingEntity target) {
        Move move=PokemonUtils.getRangeAttackMove(pokemonEntity);
        AbstractPokemonProjectile bullet;
        PokemonUtils.sendAnimationPacket(pokemonEntity, "special");

        if (move != null) {
            String moveName = move.getName();
            //CobblemonFightOrFlight.LOGGER.info(moveName);
            Random rand = new Random();
            boolean b1 = Arrays.stream(CobblemonFightOrFlight.moveConfig().single_bullet_moves).toList().contains(moveName);
            boolean b2 = Arrays.stream(CobblemonFightOrFlight.moveConfig().multiple_bullet_moves).toList().contains(moveName);
            boolean b3 = Arrays.stream(CobblemonFightOrFlight.moveConfig().single_tracing_bullet_moves).toList().contains(moveName);
            boolean b4 = Arrays.stream(CobblemonFightOrFlight.moveConfig().multiple_tracing_bullet_moves).toList().contains(moveName);
            boolean b5 = Arrays.stream(CobblemonFightOrFlight.moveConfig().single_beam_moves).toList().contains(moveName);
            boolean b6 = PokemonUtils.isExplosiveMove(moveName);
            boolean b7 = Arrays.stream(CobblemonFightOrFlight.moveConfig().sound_based_moves).toList().contains(moveName);
            if (b3 || b4) {
                for (int i = 0; i < (b3 ? 1 : rand.nextInt(3) + 1); ++i) {
                    bullet = new PokemonTracingBullet(livingEntity.level(), pokemonEntity, target, livingEntity.getDirection().getAxis());
                    addProjectileEntity(bullet, move);
                }
            } else if (b1 || b2) {
                for (int i = 0; i < (b1 ? 1 : rand.nextInt(3) + 1); ++i) {
                    bullet = new PokemonBullet(livingEntity.level(), pokemonEntity, target);
                    shootProjectileEntity(bullet);
                    addProjectileEntity(bullet, move);
                }
            } else if (b5 || b7) {
                target.hurt(pokemonEntity.damageSources().mobAttack(pokemonEntity), PokemonAttackEffect.calculatePokemonDamage(pokemonEntity, move));
                PokemonUtils.setHurtByPlayer(pokemonEntity, target);
                PokemonAttackEffect.applyOnHitEffect(pokemonEntity, target, move);
            } else if (b6) {
                //Nothing to do now.
            } else {
                bullet = new PokemonArrow(livingEntity.level(), pokemonEntity, target);
                shootProjectileEntity(bullet);
                addProjectileEntity(bullet, move);
            }
        } else {
            bullet = new PokemonArrow(livingEntity.level(), pokemonEntity, target);
            shootProjectileEntity(bullet);
            addProjectileEntity(bullet);
        }
        PokemonAttackEffect.applyPostEffect(pokemonEntity, target, move);
    }
}
