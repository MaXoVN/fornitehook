package me.fortnitehook.features.modules.combat;

import me.fortnitehook.features.modules.player.FortniteMine;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraft.util.math.BlockPos;
import me.fortnitehook.features.modules.Module;

public class BreakCheck extends Module
{
    private static BreakCheck INSTANCE;
    public BlockPos BrokenPos;

    public BreakCheck() {
        super("BreakCheck", "Check instant mine.", Category.COMBAT, true, false, false);
        this.setInstance();
    }

    public static BreakCheck Instance() {
        if (BreakCheck.INSTANCE == null) {
            BreakCheck.INSTANCE = new BreakCheck();
        }
        return BreakCheck.INSTANCE;
    }

    private void setInstance() {
        BreakCheck.INSTANCE = this;
    }

    @SubscribeEvent
    public void BrokenBlock(final PlaySoundEvent event) {
        if (FortniteMine.breakPos != null && FortniteMine.breakPos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF()))) {
            return;
        }
        if (!event.getName().endsWith("hit")) {
            return;
        }
        if (event.getName().endsWith("arrow.hit")) {
            return;
        }
        if (event.getName().endsWith("stand.hit")) {
            return;
        }
        if (BreakCheck.mc.world.getBlockState(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) {
            return;
        }
        this.BrokenPos = new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
    }

    @SubscribeEvent
    public void BrokenBlock2(final PlaySoundEvent event) {
        if (FortniteMine.breakPos != null && FortniteMine.breakPos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF()))) {
            return;
        }
        if (!event.getName().endsWith("break")) {
            return;
        }
        if (event.getName().endsWith("potion.break")) {
            return;
        }
        if (BreakCheck.mc.world.getBlockState(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) {
            return;
        }
        this.BrokenPos = new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
    }

    static {
        BreakCheck.INSTANCE = new BreakCheck();
    }
}
