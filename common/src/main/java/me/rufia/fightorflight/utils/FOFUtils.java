package me.rufia.fightorflight.utils;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FOFUtils {
    public static Vec3i stringToVec3i(String data){
        if (data.startsWith("VEC3i_")) {
            Pattern p = Pattern.compile("VEC3i_([-\\d]*)_([-\\d]*)_([-\\d]*)");//I know it's not safe, but who will send other data?
            Matcher m = p.matcher(data);
            if (m.find()) {
                try {
                    int x = Integer.parseInt(m.group(1));
                    int y = Integer.parseInt(m.group(2));
                    int z = Integer.parseInt(m.group(3));
                    return new Vec3i(x,y,z);
                    //CobblemonFightOrFlight.LOGGER.info("Generated position:x: %d y: %d z: %d".formatted(x, y, z));
                } catch (NumberFormatException e) {
                    CobblemonFightOrFlight.LOGGER.warn("Failed to converse the vec");
                }
            }
        }
        return null;
    }

}
