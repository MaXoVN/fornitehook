package me.fortnitehook.features.modules.combat;

import me.fortnitehook.features.modules.player.FortniteMine;
import me.fortnitehook.features.Feature;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.OyVey;
import me.fortnitehook.manager.ModuleManager;
import me.fortnitehook.util.BlockUtil;
import me.fortnitehook.util.EntityUtil;
import me.fortnitehook.util.Util;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AntiBurrow extends Module
{
    public static BlockPos pos;
    private final Setting<Double> range;
    private final Setting<Boolean> toggle;

    public AntiBurrow() {
        super("AntiBurrow", "AntiBurrow", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<>("Range", 5.0, 1.0, 8.0));
        this.toggle = this.register(new Setting<>("Toggle", false));
    }

    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (!EntityUtil.isntValid(player, range)) {
                if (OyVey.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AutoTrap.mc.player.getDistanceSq(player);
                }
                else {
                    if (AutoTrap.mc.player.getDistanceSq(player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AutoTrap.mc.player.getDistanceSq(player);
                }
            }
        }
        return target;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (Util.mc.currentScreen instanceof GuiHopper) {
            return;
        }
        final EntityPlayer player = this.getTarget(this.range.getValue());
        if (this.toggle.getValue()) {
            this.toggle();
        }
        if (player == null) {
            return;
        }
        AntiBurrow.pos = new BlockPos(player.posX, player.posY + 0.5, player.posZ);
        if (FortniteMine.breakPos != null) {
            if (FortniteMine.breakPos.equals(AntiBurrow.pos)) {
                return;
            }
            if (FortniteMine.breakPos.equals(new BlockPos(Util.mc.player.posX, Util.mc.player.posY + 2.0, Util.mc.player.posZ))) {
                return;
            }
            if (FortniteMine.breakPos.equals(new BlockPos(Util.mc.player.posX, Util.mc.player.posY - 1.0, Util.mc.player.posZ))) {
                return;
            }
            if (ModuleManager.isModuleEnabled("AutoCev")) {
                return;
            }
            if (ModuleManager.isModuleEnabled("AutoCev1")) {
                return;
            }
            if (AntiBurrow.mc.world.getBlockState(FortniteMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
            if (ModuleManager.isModuleEnabled("Anti32k") && Util.mc.world.getBlockState(FortniteMine.breakPos).getBlock() instanceof BlockHopper) {
                return;
            }
            if (ModuleManager.isModuleEnabled("AntiShulkerBox") && Util.mc.world.getBlockState(FortniteMine.breakPos).getBlock() instanceof BlockShulkerBox) {
                return;
            }
        }
        if (Util.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.AIR && !this.isOnLiquid() && !this.isInLiquid() && Util.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.WATER && Util.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.LAVA) {
            Util.mc.player.swingArm(EnumHand.MAIN_HAND);
            if (FortniteMine.breakPos != null && FortniteMine.breakPos.getZ() == AntiBurrow.pos.getZ() && FortniteMine.breakPos.getX() == AntiBurrow.pos.getX() && FortniteMine.breakPos.getY() == AntiBurrow.pos.getY()) {
                return;
            }
            if (FortniteMine.breakPos2 != null && OyVey.moduleManager.getModuleByClass(FortniteMine.class).isEnabled() && FortniteMine.doubleBreak.getValue() && !AntiBurrow.pos.equals(FortniteMine.breakPos2) && AntiBurrow.pos.equals(FortniteMine.breakPos)) {
                FortniteMine.ondeve2(AntiBurrow.pos);
            }
            else {
                FortniteMine.ondeve(AntiBurrow.pos);
                FortniteMine.ondeve(AntiBurrow.pos);
            }
            Util.mc.playerController.onPlayerDamageBlock(AntiBurrow.pos, BlockUtil.getRayTraceFacing(AntiBurrow.pos));
        }
    }

    private boolean isOnLiquid() {
        final double y = Util.mc.player.posY - 0.03;
        for (int x = MathHelper.floor(Util.mc.player.posX); x < MathHelper.ceil(Util.mc.player.posX); ++x) {
            for (int z = MathHelper.floor(Util.mc.player.posZ); z < MathHelper.ceil(Util.mc.player.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (Util.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        final double y = Util.mc.player.posY + 0.01;
        for (int x = MathHelper.floor(Util.mc.player.posX); x < MathHelper.ceil(Util.mc.player.posX); ++x) {
            for (int z = MathHelper.floor(Util.mc.player.posZ); z < MathHelper.ceil(Util.mc.player.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int)y, z);
                if (Util.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
}