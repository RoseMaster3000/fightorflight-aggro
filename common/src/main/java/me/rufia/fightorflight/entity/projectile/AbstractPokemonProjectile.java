package me.rufia.fightorflight.entity.projectile;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPokemonProjectile extends ThrowableProjectile {


    public AbstractPokemonProjectile(EntityType<? extends AbstractPokemonProjectile> entityType, Level level) {
        super(entityType, level);
    }

    protected void initPosition(LivingEntity shooter) {
        this.setOwner(shooter);
        BlockPos blockPos = shooter.blockPosition();
        float angle = shooter.getYRot();
        //CobblemonFightOrFlight.LOGGER.info(String.valueOf(angle));
        double radius = 0.5 * shooter.getBbWidth();
        double d = (double) blockPos.getX() + 0.5 - radius * Math.sin(angle);
        double e = (double) blockPos.getY() + Math.max(0.3f, shooter.getBbHeight() * 0.67);
        double f = (double) blockPos.getZ() + 0.5 + radius * Math.cos(angle);
        this.moveTo(d, e, f, this.getYRot(), this.getXRot());
    }


    private static final EntityDataAccessor<String> type = SynchedEntityData.defineId(AbstractPokemonProjectile.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Float> damage = SynchedEntityData.defineId(AbstractPokemonProjectile.class, EntityDataSerializers.FLOAT);

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.entityData.get(type));
        compound.putFloat("Damage", this.entityData.get(damage));
    }

    public void tick() {
        super.tick();
        makeParticle(4);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(type, compound.getString("Type"));
        this.entityData.set(damage, compound.getFloat("Damage"));
    }

    protected void makeParticle(int particleAmount) {
        if (getElementalType() == null) {
            return;
        }
        PokemonAttackEffect.makeTypeEffectParticle(particleAmount, this, getElementalType());
    }

    public float getDamage() {
        return this.entityData.get(damage);
    }

    public void setDamage(float Damage) {
        this.entityData.set(damage, Damage);
    }

    public String getElementalType() {
        return this.entityData.get(type);
    }

    public void setElementalType(String Type) {
        this.entityData.set(type, Type);
    }

    public void applyTypeEffect(PokemonEntity pokemonEntity, LivingEntity hurtTarget) {
        if (!Objects.equals(getElementalType(), pokemonEntity.getPokemon().getPrimaryType().getName())) {
            PokemonAttackEffect.applyTypeEffect(pokemonEntity, hurtTarget, getElementalType());
        } else {
            PokemonAttackEffect.applyTypeEffect(pokemonEntity, hurtTarget);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(type, "normal");
        this.entityData.define(damage, 1f);
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity owner = getOwner();
        Entity target = result.getEntity();
        if (owner instanceof PokemonEntity pokemonEntity) {
            if (this instanceof ExplosivePokemonProjectile) {
                explode(pokemonEntity);
                return;
            }

            PokemonUtils.setHurtByPlayer(pokemonEntity, target);
            PokemonAttackEffect.applyOnHitEffect(pokemonEntity, target, PokemonUtils.getMove(pokemonEntity));
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        if (this instanceof ExplosivePokemonProjectile) {
            BlockPos blockPos = new BlockPos(result.getBlockPos());
            this.level().getBlockState(blockPos).entityInside(this.level(), blockPos, this);
            if (!this.level().isClientSide() && getOwner() instanceof PokemonEntity pokemonEntity) {
                this.explode(pokemonEntity);
            }
        }
        super.onHitBlock(result);
    }

    private void explode(PokemonEntity owner) {
        level().broadcastEntityEvent(this, (byte) 17);
        this.gameEvent(GameEvent.EXPLODE, this.getOwner());
        this.dealExplosionDamage(owner);
        this.discard();
    }

    private void dealExplosionDamage(PokemonEntity owner) {
        double radiusMultiplier = 2.0;
        //Vec3 vec3 = this.position();
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radiusMultiplier));
        Iterator<LivingEntity> it = list.iterator();
        while (true) {
            LivingEntity livingEntity;
            do {
                if (!it.hasNext()) {
                    return;
                }
                livingEntity = it.next();
            } while (this.distanceToSqr(livingEntity) > 25.0);
            float fullDamageRadius = 0.4f;
            float dmgMultiplier;
            if (this.distanceToSqr(livingEntity) < fullDamageRadius) {
                dmgMultiplier = 1.0f;
            } else {
                dmgMultiplier = 1 - (float) (distanceTo(livingEntity) / (getBbWidth() * radiusMultiplier));
            }
            //CobblemonFightOrFlight.LOGGER.info(livingEntity.getDisplayName().getString());
            boolean bl = livingEntity.hurt(this.damageSources().mobProjectile(this, owner), getDamage() * dmgMultiplier);
            if (bl) {
                applyTypeEffect(owner, livingEntity);
            }
            PokemonUtils.setHurtByPlayer(owner, livingEntity);
            PokemonAttackEffect.applyOnHitEffect(owner, livingEntity, PokemonUtils.getMove(owner));
        }
    }


    public void accurateShoot(double x, double y, double z, float velocity, float inaccuracy) {
        double horizontalDistance = Math.sqrt(x * x + z * z);
        float g = getGravity();
        double v2 = velocity * velocity;
        double delta = Math.sqrt(2 * v2 * g * y + v2 * v2 - g * g * horizontalDistance * horizontalDistance);
        double t = Math.sqrt(2 * (g * y + v2 - delta)) / g;
        double result = y + 0.5 * g * t * t;
        this.shoot(x, result, z, velocity, inaccuracy);
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }
}
