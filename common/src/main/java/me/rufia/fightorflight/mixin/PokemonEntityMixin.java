package me.rufia.fightorflight.mixin;


import com.cobblemon.mod.common.entity.Poseable;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin extends Mob implements Shearable ,Poseable{
    @Nullable
    private LivingEntity fightorflight$clientSideCachedAttackTarget;
    private int fightorflight$clientSideAttackTime;
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    static {
        DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(PokemonEntityMixin.class, EntityDataSerializers.INT);
    }

    protected PokemonEntityMixin(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public LivingEntity getTarget() {
        //CobblemonFightOrFlight.LOGGER.info("TARGET ACCESS");
        if (this.level().isClientSide) {
            //CobblemonFightOrFlight.LOGGER.info("CLIENT SIDE DETECTED");
            if (fightorflight$clientSideCachedAttackTarget != null) {
                //CobblemonFightOrFlight.LOGGER.info("CLIENT TARGET TRANSPORTED");
                return fightorflight$clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity((Integer) this.entityData.get(DATA_ID_ATTACK_TARGET));
                //CobblemonFightOrFlight.LOGGER.info("TARGET INIT");
                if (entity instanceof LivingEntity) {
                    fightorflight$clientSideCachedAttackTarget = (LivingEntity) entity;
                    //CobblemonFightOrFlight.LOGGER.info("TARGET INIT AND TRANSPORTED");
                    return fightorflight$clientSideCachedAttackTarget;
                }
            }
        }
        return  super.getTarget();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_ID_ATTACK_TARGET.equals(key)) {
            this.fightorflight$clientSideAttackTime = 0;
            this.fightorflight$clientSideCachedAttackTarget = null;
        }

    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
    }

    public void setTarget(LivingEntity target){
        super.setTarget(target);
        if(target!=null){
            this.entityData.set(DATA_ID_ATTACK_TARGET,target.getId());
            //CobblemonFightOrFlight.LOGGER.info("TARGET SET");
            //CobblemonFightOrFlight.LOGGER.info(target.getEncodeId());
        }
    }
}
