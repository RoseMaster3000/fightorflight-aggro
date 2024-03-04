package me.rufia.fightorflight.mixin;


import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.entity.Poseable;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.item.ItemFightOrFlight;
import me.rufia.fightorflight.item.PokeStaff;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin extends Mob implements PokemonInterface {
    @Unique
    @Nullable
    private LivingEntity fightorflight$clientSideCachedAttackTarget;
    @Unique
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    @Unique
    private static final EntityDataAccessor<Integer> ATTACK_TIME;
    @Unique
    private static final EntityDataAccessor<String> MOVE;

    static {
        DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(PokemonEntityMixin.class, EntityDataSerializers.INT);
        ATTACK_TIME=SynchedEntityData.defineId(PokemonEntityMixin.class,EntityDataSerializers.INT);
        MOVE=SynchedEntityData.defineId(PokemonEntityMixin.class,EntityDataSerializers.STRING);
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
        return  super.getTarget();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_ID_ATTACK_TARGET.equals(key)) {
            this.fightorflight$clientSideCachedAttackTarget = null;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
        this.entityData.define(ATTACK_TIME,0);
        this.entityData.define(MOVE,"");
    }

    public void setTarget(LivingEntity target){
        super.setTarget(target);
        if(target!=null){
            this.entityData.set(DATA_ID_ATTACK_TARGET,target.getId());
        }
    }
    @Override
    public int getAttackTime(){
         return entityData.get(ATTACK_TIME);
    }
    @Override
    public void setAttackTime(int val){
        entityData.set(ATTACK_TIME,val);
    }
    @Override
    public boolean usingBeam(){
        if(entityData.get(MOVE).equals("")){return false;}
        return Arrays.stream(CobblemonFightOrFlight.moveConfig().single_beam_moves).toList().contains(getCurrentMove());
    }
    @Override
    public void setCurrentMove(Move move){
        entityData.set(MOVE,move.getName());
    }
    @Override
    public String getCurrentMove(){
        return entityData.get(MOVE);
    }
            @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.is(ItemFightOrFlight.POKESTAFF.get())){
            PokeStaff staff=(PokeStaff) itemStack.getItem();
            if(staff.canSend(itemStack)){
                staff.send(player,this,itemStack);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }


}
