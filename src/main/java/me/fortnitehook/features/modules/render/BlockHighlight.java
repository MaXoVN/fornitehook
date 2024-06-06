package me.fortnitehook.features.modules.render;

import me.fortnitehook.util.RenderUtil;
import me.fortnitehook.event.events.Render3DEvent;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.util.ColorUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight
        extends Module {
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> cAlpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, ClickGui.getInstance().rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(ClickGui.INSTANCE().Color.getValue().getRed(), ClickGui.INSTANCE().Color.getValue().getGreen(), ClickGui.INSTANCE().Color.getValue().getBlue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), false);
        }
    }
}

