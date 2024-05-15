package me.rufia.fightorflight.entity.projectile;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.entity.EntityFightOrFlight;
import me.rufia.fightorflight.entity.projectile.AbstractPokemonProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class PokemonArrow extends AbstractPokemonProjectile {
    protected int knockback;

    public PokemonArrow(EntityType<? extends AbstractPokemonProjectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public PokemonArrow(Level level, LivingEntity shooter, Entity finalTarget) {
        super(EntityFightOrFlight.ARROW_PROJECTILE.get(), level);
        this.setOwner(shooter);
        BlockPos blockPos = shooter.blockPosition();
        double d = (double) blockPos.getX() + 0.5;
        double e = (double) blockPos.getY() + Math.max(0.5f, shooter.getBbHeight() / 2);
        double f = (double) blockPos.getZ() + 0.5;
        this.moveTo(d, e, f, this.getYRot(), this.getXRot());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    public void lerpTo(double x, double y, double z, float yRot, float xRot, int lerpSteps, boolean teleport) {
        this.setPos(x, y, z);
        this.setRot(yRot, xRot);
    }

    public void lerpMotion(double x, double y, double z) {
        super.lerpMotion(x, y, z);
    }

    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
    }

    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875));
            this.setXRot((float)(Mth.atan2(vec3.y, d) * 57.2957763671875));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        double e = vec3.x;
        double f = vec3.y;
        double g = vec3.z;

        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;
        double l = vec3.horizontalDistance();
        if (this.noPhysics) {
            this.setYRot((float) (Mth.atan2(-e, -g) * 57.2957763671875));
        } else {
            this.setYRot((float) (Mth.atan2(e, g) * 57.2957763671875));
        }

        this.setXRot((float) (Mth.atan2(f, l) * 57.2957763671875));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        float m = 0.99F;
        float n = 0.05F;
        if (this.isInWater()) {
            for (int o = 0; o < 4; ++o) {
                float p = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, h - e * 0.25, j - f * 0.25, k - g * 0.25, e, f, g);
            }

            m = this.getWaterInertia();
        }
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }
        this.setDeltaMovement(vec3.scale((double) m));
        if (!this.isNoGravity() && !this.noPhysics) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - 0.05, vec34.z);
        }

        this.setPos(h, j, k);
        this.checkInsideBlocks();
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity entity2 = this.getOwner();
        DamageSource damageSource;
        float f = (float) this.getDeltaMovement().length();

        if (entity2 instanceof LivingEntity livingEntity) {
            damageSource = this.damageSources().mobAttack(livingEntity);
            livingEntity.setLastHurtMob(target);
        } else {
            damageSource = this.damageSources().generic();
        }

        boolean bl = target.getType() == EntityType.ENDERMAN;

        if (target.hurt(damageSource, getDamage())) {
            if (bl) {
                return;
            }
            if (target instanceof LivingEntity livingEntity) {
                if (this.knockback > 0) {
                    double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale((double) this.knockback * 0.6 * d);
                    if (vec3.lengthSqr() > 0.0) {
                        livingEntity.push(vec3.x, 0.1, vec3.z);
                    }
                }
                if (entity2 instanceof PokemonEntity pokemonEntity) {
                    applyTypeEffect(pokemonEntity, livingEntity);
                }
            }
            this.discard();
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
        }

    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Vec3 vec3 = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec32 = vec3.normalize().scale(0.05000000074505806);
        this.setPosRaw(this.getX() - vec32.x, this.getY() - vec32.y, this.getZ() - vec32.z);
        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.discard();
    }

    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target);
    }
    protected float getWaterInertia() {
        return 0.6F;
    }
}