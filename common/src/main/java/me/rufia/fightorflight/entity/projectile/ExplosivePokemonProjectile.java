package me.rufia.fightorflight.entity.projectile;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Iterator;
import java.util.List;

public abstract class ExplosivePokemonProjectile extends AbstractPokemonProjectile {
    public ExplosivePokemonProjectile(EntityType<? extends AbstractPokemonProjectile> entityType, Level level) {
        super(entityType, level);
    }

    protected void explode(PokemonEntity owner) {
        level().broadcastEntityEvent(this, (byte) 17);
        if (!this.level().isClientSide) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, 0.0);
        }
        this.gameEvent(GameEvent.EXPLODE, this.getOwner());
        this.dealExplosionDamage(owner);
        this.discard();
    }

    protected void dealExplosionDamage(PokemonEntity owner) {
        Move move = PokemonUtils.getRangeAttackMove(owner);
        if (move == null) {
            return;
        }
        double radius = getRadius(owner, move);
        //Vec3 vec3 = this.position();
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius - getBbWidth() / 2));
        Iterator<LivingEntity> it = list.iterator();
        while (true) {
            LivingEntity livingEntity;
            do {
                if (!it.hasNext()) {
                    return;
                }
                livingEntity = it.next();
            } while (this.distanceToSqr(livingEntity) > 25.0);
            float fullDamageRadius = 0.8f;
            float dmgMultiplier;
            if (this.distanceToSqr(livingEntity) <= fullDamageRadius) {
                dmgMultiplier = 1.0f;
            } else {
                dmgMultiplier = Math.max(1 - (float) (distanceTo(livingEntity) / (radius)), CobblemonFightOrFlight.moveConfig().min_AoE_damage_multiplier);
            }
            //CobblemonFightOrFlight.LOGGER.info(livingEntity.getDisplayName().getString());
            boolean bl = livingEntity.hurt(this.damageSources().mobProjectile(this, owner), getDamage() * dmgMultiplier);
            if (bl) {
                applyTypeEffect(owner, livingEntity);
            }
            PokemonUtils.setHurtByPlayer(owner, livingEntity);
            PokemonAttackEffect.applyOnHitEffect(owner, livingEntity, move);
        }
    }

    private double getRadius(PokemonEntity owner, Move move) {
        Pokemon pokemon = owner.getPokemon();
        boolean isSpecial = move.getDamageCategory().equals(DamageCategories.INSTANCE.getSPECIAL());
        int stat = isSpecial ? pokemon.getSpecialAttack() : pokemon.getAttack();
        int requiredStat = isSpecial ? CobblemonFightOrFlight.commonConfig().maximum_special_attack_stat : CobblemonFightOrFlight.commonConfig().maximum_attack_stat;
        return Math.min(Mth.lerp((float) stat / requiredStat, CobblemonFightOrFlight.moveConfig().min_AoE_radius, CobblemonFightOrFlight.moveConfig().max_AoE_radius), CobblemonFightOrFlight.moveConfig().max_AoE_radius);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BlockPos blockPos = new BlockPos(result.getBlockPos());
        this.level().getBlockState(blockPos).entityInside(this.level(), blockPos, this);
        if (!this.level().isClientSide() && getOwner() instanceof PokemonEntity pokemonEntity) {
            this.explode(pokemonEntity);
        }
        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity owner = getOwner();
        if (owner instanceof PokemonEntity pokemonEntity) {
            explode(pokemonEntity);
        }
    }
}
