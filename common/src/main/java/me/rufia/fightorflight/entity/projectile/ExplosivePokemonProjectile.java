package me.rufia.fightorflight.entity.projectile;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import me.rufia.fightorflight.utils.PokemonUtils;
import me.rufia.fightorflight.utils.explosion.FOFExplosion;
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
        if (owner == null) {
            return;
        }
        FOFExplosion explosion = FOFExplosion.createExplosion(this, owner, getX(), getY(), getZ(), true);
        if (explosion != null) {
            explosion.explode();
            explosion.finalizeExplosion();
        }
        /*
        Move move = PokemonUtils.getMove(owner);
        boolean isSpecial = false;
        if (move != null) {
            isSpecial = move.getDamageCategory().equals(DamageCategories.INSTANCE.getSPECIAL());
        }
        PokemonAttackEffect.dealAoEDamage(owner, this, isSpecial, true);

         */
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
