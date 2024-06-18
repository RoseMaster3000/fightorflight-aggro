package me.rufia.fightorflight.entity.projectile;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Objects;

public abstract class AbstractPokemonProjectile extends ThrowableProjectile {


    public AbstractPokemonProjectile(EntityType<? extends AbstractPokemonProjectile> entityType, Level level) {
        super(entityType, level);

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
            PokemonUtils.setHurtByPlayer(pokemonEntity, target);
        }
    }

    public void accurateShoot(double x, double y, double z, float velocity, float inaccuracy) {
        double horizontalDistance = Math.sqrt(x * x + z * z);
        float g = getGravity();
        double a = velocity * velocity - g;
        double delta = a * a - 0.5 * g * (horizontalDistance * horizontalDistance + y * y);
        double b = a - Math.sqrt(delta);
        double t = Math.sqrt(b) / Math.sqrt(g * g / 2);
        double result = Math.sqrt(velocity * velocity - horizontalDistance / t);
        if (y < 0) {
            result = -result;
        }
        this.shoot(x, result, z, velocity, inaccuracy);
    }
}
