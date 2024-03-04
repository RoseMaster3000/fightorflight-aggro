package me.rufia.fightorflight.entity;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


public class PokemonTracingBullet extends AbstractPokemonProjectile {
    private static final double SPEED = 0.3;
    private static final int min_interval = 3;
    private static final int random_interval = 2;
    @Nullable
    private Entity finalTarget;
    @Nullable
    private Direction currentMoveDirection;
    private int flightSteps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID targetId;

    public PokemonTracingBullet(EntityType<? extends PokemonTracingBullet> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public PokemonTracingBullet(Level level, LivingEntity shooter, Entity finalTarget, Direction.Axis axis) {
        super(EntityFightOrFlight.TRACING_BULLET.get(), level);
        this.setOwner(shooter);
        BlockPos blockPos = shooter.blockPosition();
        double d = (double) blockPos.getX() + 0.5;
        double e = (double) blockPos.getY() + Math.max(0.5f, shooter.getBbHeight() / 2);
        double f = (double) blockPos.getZ() + 0.5;
        this.moveTo(d, e, f, this.getYRot(), this.getXRot());
        this.finalTarget = finalTarget;
        this.currentMoveDirection = Direction.UP;
        this.selectNextMoveDirection(axis);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {

        super.defineSynchedData();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.finalTarget != null) {
            compound.putUUID("Target", this.finalTarget.getUUID());
        }
        if (this.currentMoveDirection != null) {
            compound.putInt("Dir", this.currentMoveDirection.get3DDataValue());
        }
        compound.putInt("Steps", this.flightSteps);
        compound.putDouble("TXD", this.targetDeltaX);
        compound.putDouble("TYD", this.targetDeltaY);
        compound.putDouble("TZD", this.targetDeltaZ);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.flightSteps = compound.getInt("Steps");
        this.targetDeltaX = compound.getDouble("TXD");
        this.targetDeltaY = compound.getDouble("TYD");
        this.targetDeltaZ = compound.getDouble("TZD");
        if (compound.contains("Dir", 99)) {
            this.currentMoveDirection = Direction.from3DDataValue(compound.getInt("Dir"));
        }

        if (compound.hasUUID("Target")) {
            this.targetId = compound.getUUID("Target");
        }
    }

    @Nullable
    private Direction getMoveDirection() {
        return this.currentMoveDirection;
    }

    private void setMoveDirection(@Nullable Direction direction) {
        this.currentMoveDirection = direction;
    }

    private void selectNextMoveDirection(@Nullable Direction.Axis axis) {
        double d = 0.5;
        BlockPos blockPos;
        if (this.finalTarget == null) {
            blockPos = this.blockPosition().below();
        } else {
            d = (double) this.finalTarget.getBbHeight() * 0.5;
            blockPos = BlockPos.containing(this.finalTarget.getX(), this.finalTarget.getY() + d, this.finalTarget.getZ());
        }

        double e = (double) blockPos.getX() + 0.5;
        double f = (double) blockPos.getY() + d;
        double g = (double) blockPos.getZ() + 0.5;
        Direction direction = null;
        if (!blockPos.closerToCenterThan(this.position(), 2.0)) {
            BlockPos blockPos2 = this.blockPosition();
            List<Direction> list = Lists.newArrayList();
            if (axis != Direction.Axis.X) {
                if (blockPos2.getX() < blockPos.getX() && this.level().isEmptyBlock(blockPos2.east())) {
                    list.add(Direction.EAST);
                } else if (blockPos2.getX() > blockPos.getX() && this.level().isEmptyBlock(blockPos2.west())) {
                    list.add(Direction.WEST);
                }
            }

            if (axis != Direction.Axis.Y) {
                if (blockPos2.getY() < blockPos.getY() && this.level().isEmptyBlock(blockPos2.above())) {
                    list.add(Direction.UP);
                } else if (blockPos2.getY() > blockPos.getY() && this.level().isEmptyBlock(blockPos2.below())) {
                    list.add(Direction.DOWN);
                }
            }

            if (axis != Direction.Axis.Z) {
                if (blockPos2.getZ() < blockPos.getZ() && this.level().isEmptyBlock(blockPos2.south())) {
                    list.add(Direction.SOUTH);
                } else if (blockPos2.getZ() > blockPos.getZ() && this.level().isEmptyBlock(blockPos2.north())) {
                    list.add(Direction.NORTH);
                }
            }

            direction = Direction.getRandom(this.random);
            if (list.isEmpty()) {
                for (int i = 5; !this.level().isEmptyBlock(blockPos2.relative(direction)) && i > 0; --i) {
                    direction = Direction.getRandom(this.random);
                }
            } else {
                direction = (Direction) list.get(this.random.nextInt(list.size()));
            }

            e = this.getX() + (double) direction.getStepX();
            f = this.getY() + (double) direction.getStepY();
            g = this.getZ() + (double) direction.getStepZ();
        }

        this.setMoveDirection(direction);
        double h = e - this.getX();
        double j = f - this.getY();
        double k = g - this.getZ();
        double l = Math.sqrt(h * h + j * j + k * k);
        if (l == 0.0) {
            this.targetDeltaX = 0.0;
            this.targetDeltaY = 0.0;
            this.targetDeltaZ = 0.0;
        } else {
            this.targetDeltaX = h / l * SPEED;
            this.targetDeltaY = j / l * SPEED;
            this.targetDeltaZ = k / l * SPEED;
        }

        this.hasImpulse = true;
        this.flightSteps = min_interval + this.random.nextInt(random_interval) * 3;
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }
    }


    public void tick() {
        super.tick();
        Vec3 vec3;
        if (!this.level().isClientSide) {
            if (this.finalTarget == null && this.targetId != null) {
                this.finalTarget = ((ServerLevel) this.level()).getEntity(this.targetId);
                if (this.finalTarget == null) {
                    this.targetId = null;
                }
            }

            if (this.finalTarget == null || !this.finalTarget.isAlive() || this.finalTarget instanceof Player && this.finalTarget.isSpectator()) {
                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
                }
            } else {
                this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025, -1.0, 1.0);
                this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025, -1.0, 1.0);
                this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025, -1.0, 1.0);
                vec3 = this.getDeltaMovement();
                this.setDeltaMovement(vec3.add((this.targetDeltaX - vec3.x) * 0.2, (this.targetDeltaY - vec3.y) * 0.2, (this.targetDeltaZ - vec3.z) * 0.2));
            }

            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onHit(hitResult);
            }
        }

        this.checkInsideBlocks();
        vec3 = this.getDeltaMovement();
        this.setPos(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
        if (this.level().isClientSide) {
            makeParticle(4);
            //this.level().addParticle(ParticleTypes.END_ROD, this.getX() - vec3.x, this.getY() - vec3.y + 0.15, this.getZ() - vec3.z, 0.0, 0.0, 0.0);
        } else if (this.finalTarget != null && !this.finalTarget.isRemoved()) {
            if (this.flightSteps > 0) {
                --this.flightSteps;
                if (this.flightSteps == 0) {
                    this.selectNextMoveDirection(this.currentMoveDirection == null ? null : this.currentMoveDirection.getAxis());
                }
            }

            if (this.currentMoveDirection != null) {
                BlockPos blockPos = this.blockPosition();
                Direction.Axis axis = this.currentMoveDirection.getAxis();
                if (this.level().loadedAndEntityCanStandOn(blockPos.relative(this.currentMoveDirection), this)) {
                    this.selectNextMoveDirection(axis);
                } else {
                    BlockPos blockPos2 = this.finalTarget.blockPosition();
                    if (axis == Direction.Axis.X && blockPos.getX() == blockPos2.getX() || axis == Direction.Axis.Z && blockPos.getZ() == blockPos2.getZ() || axis == Direction.Axis.Y && blockPos.getY() == blockPos2.getY()) {
                        this.selectNextMoveDirection(axis);
                    }
                }
            }
        }

    }
    protected void makeParticle(int particleAmount) {
        if (getElementalType() == null) {
            return;
        }
        if (particleAmount > 0) {
            double d = 0;
            double e = 0;
            double f = 0;
            if (this.level().isClientSide) {
                for (int j = 0; j < particleAmount; ++j) {
                    this.level().addParticle(PokemonAttackEffect.getParticleFromType(getElementalType()), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), d, e, f);
                }
            }
        }
    }
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && !target.noPhysics;
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 16384.0;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity entity2 = this.getOwner();
        LivingEntity livingEntity = entity2 instanceof LivingEntity ? (LivingEntity) entity2 : null;
        boolean bl = entity.hurt(this.damageSources().mobProjectile(this, livingEntity), getDamage());
        if (bl) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity) entity;
                applyTypeEffect((PokemonEntity) livingEntity, livingEntity2);
                //livingEntity2.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200), (Entity) MoreObjects.firstNonNull(entity2, this));
            }
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, 0.0);
        }//this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
    }

    private void destroy() {
        this.discard();
        this.level().gameEvent(GameEvent.ENTITY_DAMAGE, this.position(), GameEvent.Context.of(this));
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        this.destroy();
    }

    public boolean isPickable() {
        return true;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            //this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
            this.destroy();
        }
        return true;
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        double d = packet.getXa();
        double e = packet.getYa();
        double f = packet.getZa();
        this.setDeltaMovement(d, e, f);
    }
}
