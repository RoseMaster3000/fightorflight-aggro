package me.rufia.fightorflight.utils;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.item.component.PokeStaffComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FOFUtils {
    public static Vec3i stringToVec3i(String data) {
        if (data.startsWith("VEC3i_")) {
            Pattern p = Pattern.compile("VEC3i_([-\\d]*)_([-\\d]*)_([-\\d]*)");//I know it's not safe, but who will send other data?
            Matcher m = p.matcher(data);
            if (m.find()) {
                try {
                    int x = Integer.parseInt(m.group(1));
                    int y = Integer.parseInt(m.group(2));
                    int z = Integer.parseInt(m.group(3));
                    return new Vec3i(x, y, z);
                    //CobblemonFightOrFlight.LOGGER.info("Generated position:x: %d y: %d z: %d".formatted(x, y, z));
                } catch (NumberFormatException e) {
                    CobblemonFightOrFlight.LOGGER.warn("Failed to converse the vec");
                }
            }
        }
        return null;
    }

    public static String createCommandData(Player player, PokeStaffComponent.CMDMODE cmdmode) {
        String cmdData = "";
        switch (cmdmode) {
            case MOVE, ATTACK_POSITION, MOVE_ATTACK -> {
                BlockHitResult result = RayTrace.rayTraceBlock(player, 16);
                BlockPos blockPos = result.getBlockPos();
                //CobblemonFightOrFlight.LOGGER.info("VEC3_%s_%s_%s".formatted(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                cmdData = "VEC3i_%s_%s_%s".formatted(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
            case ATTACK -> {
                LivingEntity livingEntity = RayTrace.rayTraceEntity(player, 16);
                if (livingEntity != null) {
                    cmdData = "ENTITY_%s".formatted(livingEntity.getStringUUID());
                    //CobblemonFightOrFlight.LOGGER.info("ENTITY_%s".formatted(livingEntity.getStringUUID()));
                }
            }
            case STAY -> cmdData = "POS_SELF";
            default -> cmdData = "";
        }
        return cmdData;
    }
}
