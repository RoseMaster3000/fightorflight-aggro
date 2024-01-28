package me.rufia.fightorflight.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

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
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(type, compound.getString("Type"));
        this.entityData.set(damage, compound.getFloat("Damage"));

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

    @Override
    protected void defineSynchedData() {
        this.entityData.define(type,"normal");
        this.entityData.define(damage,1f);
    }
}
