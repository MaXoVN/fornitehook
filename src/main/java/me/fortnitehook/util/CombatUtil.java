package me.fortnitehook.util;

import me.fortnitehook.api.Minecraftable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class CombatUtil
        implements Minecraftable {
    public static EntityPlayer getTarget(float f) {
        EntityPlayer entityPlayer = null;
        int n = CombatUtil.mc.world.playerEntities.size();
        for (int i = 0; i < n; ++i) {
            EntityPlayer entityPlayer2 = CombatUtil.mc.world.playerEntities.get(i);
            if (EntityUtil.isntValid(entityPlayer2, f)) continue;
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                continue;
            }
            if (!(CombatUtil.mc.player.getDistanceSq(entityPlayer2) < CombatUtil.mc.player.getDistanceSq(entityPlayer))) continue;
            entityPlayer = entityPlayer2;
        }
        return entityPlayer;
    }

    public static boolean isInHole(EntityPlayer entityPlayer) {
        return CombatUtil.isBlockValid(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return CombatUtil.isBedrockHole(blockPos) || CombatUtil.isObbyHole(blockPos) || CombatUtil.isBothHole(blockPos);
    }

    public static int isInHoleInt(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(entityPlayer.getPositionVector());
        if (CombatUtil.isBedrockHole(blockPos)) {
            return 1;
        }
        if (CombatUtil.isObbyHole(blockPos) || CombatUtil.isBothHole(blockPos)) {
            return 2;
        }
        return 0;
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] blockPosArray;
        for (BlockPos blockPos2 : blockPosArray = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState iBlockState = CombatUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] blockPosArray;
        for (BlockPos blockPos2 : blockPosArray = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState iBlockState = CombatUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] blockPosArray;
        for (BlockPos blockPos2 : blockPosArray = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState iBlockState = CombatUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.BEDROCK || iBlockState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }
}