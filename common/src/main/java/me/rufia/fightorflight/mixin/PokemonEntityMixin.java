package me.rufia.fightorflight.mixin;


import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.experience.SidemodExperienceSource;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.item.ItemFightOrFlight;
import me.rufia.fightorflight.item.PokeStaff;
import me.rufia.fightorflight.utils.FOFEVCalculator;
import me.rufia.fightorflight.utils.FOFExpCalculator;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Map;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin extends Mob implements PokemonInterface {
    @Shadow
    public abstract void cry();

    @Unique
    @Nullable
    private LivingEntity fightorflight$clientSideCachedAttackTarget;
    @Unique
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    @Unique
    private static final EntityDataAccessor<Integer> ATTACK_TIME;
    @Unique
    private static final EntityDataAccessor<String> MOVE;
    @Unique
    private static final EntityDataAccessor<Integer> CRY_CD;

    static {
        DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(PokemonEntityMixin.class, EntityDataSerializers.INT);
        ATTACK_TIME = SynchedEntityData.defineId(PokemonEntityMixin.class, EntityDataSerializers.INT);
        MOVE = SynchedEntityData.defineId(PokemonEntityMixin.class, EntityDataSerializers.STRING);
        CRY_CD = SynchedEntityData.defineId(PokemonEntityMixin.class, EntityDataSerializers.INT);
    }

    protected PokemonEntityMixin(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public LivingEntity getTarget() {
        if (this.level().isClientSide) {
            if (fightorflight$clientSideCachedAttackTarget != null) {
                return fightorflight$clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity((Integer) this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    fightorflight$clientSideCachedAttackTarget = (LivingEntity) entity;
                    return fightorflight$clientSideCachedAttackTarget;
                }
            }
        }
        return super.getTarget();
    }

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    public void onSyncedDataUpdated(EntityDataAccessor<?> key, CallbackInfo ci) {
        if (DATA_ID_ATTACK_TARGET.equals(key)) {
            this.fightorflight$clientSideCachedAttackTarget = null;
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedData(CallbackInfo info) {
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
        this.entityData.define(ATTACK_TIME, 0);
        this.entityData.define(MOVE, "");
        this.entityData.define(CRY_CD, 0);
    }

    public void setTarget(LivingEntity target) {
        super.setTarget(target);
        if (target != null) {
            this.entityData.set(DATA_ID_ATTACK_TARGET, target.getId());
        }
    }

    @Override
    public int getAttackTime() {
        return entityData.get(ATTACK_TIME);
    }

    @Override
    public void setAttackTime(int val) {
        entityData.set(ATTACK_TIME, val);
    }

    @Override
    public boolean usingBeam() {
        if (entityData.get(MOVE).equals("")) {
            return false;
        }
        return Arrays.stream(CobblemonFightOrFlight.moveConfig().single_beam_moves).toList().contains(getCurrentMove());
    }

    @Override
    public boolean usingSound() {
        if (entityData.get(MOVE).equals("")) {
            return false;
        }
        return Arrays.stream(CobblemonFightOrFlight.moveConfig().sound_based_moves).toList().contains(getCurrentMove());
    }

    @Override
    public void setCurrentMove(Move move) {
        entityData.set(MOVE, move.getName());
    }

    @Override
    public String getCurrentMove() {
        return entityData.get(MOVE);
    }

    @Override
    public int getNextCryTime() {
        return this.entityData.get(CRY_CD);
    }

    @Override
    public void setNextCryTime(int time) {
        this.entityData.set(CRY_CD, time);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        var targetEntity = getTarget();
        if (targetEntity != null && targetEntity.isAlive()) {
            if (getNextCryTime() == 0) {
                this.cry();
                if (CobblemonFightOrFlight.commonConfig().multiple_cries) {
                    setNextCryTime(CobblemonFightOrFlight.commonConfig().time_to_cry_again);
                } else {
                    setNextCryTime(-1);
                }
            }
        } else {
            setNextCryTime(0);
        }
        if(getNextCryTime()>=0){
            setNextCryTime(getNextCryTime()-1);
        }

    }

    //Don't use @Override for this function or you will find that you can't change your pokemon's held item
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void mobInteractInject(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemFightOrFlight.POKESTAFF.get())) {
            PokeStaff staff = (PokeStaff) itemStack.getItem();
            if (staff.canSend(itemStack)) {
                staff.send(player, this, itemStack);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void dropAllDeathLootInject(DamageSource source, CallbackInfo ci) {
        if (getLastHurtByMob() instanceof PokemonEntity pokemonEntity) {
            if (pokemonEntity.getOwner() != null) {
                PokemonEntity self = (PokemonEntity) (Object) this;
                pokemonEntity.getPokemon().addExperience(new SidemodExperienceSource(CobblemonFightOrFlight.MODID), FOFExpCalculator.calculate(pokemonEntity.getPokemon(), self.getPokemon()));
                if (CobblemonFightOrFlight.commonConfig().can_gain_ev) {
                    var map = FOFEVCalculator.calculate(pokemonEntity.getPokemon(), self.getPokemon());
                    for (Map.Entry<Stat, Integer> entry : map.entrySet()) {
                        pokemonEntity.getPokemon().getEvs().add(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }
}
