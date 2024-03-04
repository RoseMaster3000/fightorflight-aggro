package me.rufia.fightorflight.utils;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.PokemonInterface;

public class PokemonUtils {
    public static boolean shouldMelee(PokemonEntity pokemonEntity){
        Move move=getMove(pokemonEntity,false);
        boolean b1=pokemonEntity.getPokemon().getAttack()>pokemonEntity.getPokemon().getSpecialAttack();//The default setting.
        boolean b2=pokemonEntity.getOwner()!=null;//The pokemon has a trainer.
        boolean b3=move!=null;//The trainer selected a physical move.
        if(!b2){
            return b1;//wild pokemon just choose the strongest way to attack
        }
        else {
            return b3;
        }
    }
    public static Move getMove(PokemonEntity pokemonEntity, boolean getSpecial) {
        String moveName = !(((PokemonInterface) (Object) pokemonEntity).getCurrentMove() == null) ? (((PokemonInterface) (Object) pokemonEntity).getCurrentMove()) : pokemonEntity.getPokemon().getMoveSet().get(0).getName();
        Move move = null;
        boolean flag = false;
        for (Move m : pokemonEntity.getPokemon().getMoveSet().getMoves()) {
            move = m;
            if (m.getName().equals(moveName)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            move = pokemonEntity.getPokemon().getMoveSet().get(0);

        }
        if (move == null) {
            return null;
        }
        boolean isSpecial = move.getDamageCategory() == DamageCategories.INSTANCE.getSPECIAL();
        boolean isPhysical= move.getDamageCategory()==DamageCategories.INSTANCE.getPHYSICAL();
        if (isSpecial&&getSpecial) {
            ((PokemonInterface) (Object ) pokemonEntity).setCurrentMove(move);
            return move;
        } else if (isPhysical&&!getSpecial) {
            ((PokemonInterface) (Object ) pokemonEntity).setCurrentMove(move);
            return move;
        }
        return null;
    }
}
