package me.fortnitehook.features.modules.render;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.fortnitehook.event.events.Render3DEvent;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.event.events.*;
import me.fortnitehook.util.ColorUtil;
import me.fortnitehook.util.RenderUtil;
import me.fortnitehook.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class CrystalSpawns
        extends Module {
    private static CrystalSpawns INSTANCE = new CrystalSpawns();
    public final Setting<Enum> Mode = this.register(new Setting("RenderMode", RenderMode.EUROPA));
    public final Setting<Float> increase = this.register(new Setting<Float>("Increase Size", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v ->this.Mode.getValue()== RenderMode.OCTO));
    public final Setting<Integer> riseSpeed = this.register(new Setting<Integer>("Rise Time", 5, 1, 50, v ->this.Mode.getValue()== RenderMode.OCTO));
    private final Setting<Boolean> Rainbow = this.register(new Setting<Object>("Rainbow", false));
    public Setting<java.awt.Color> Color = register(new Setting("Color", new Color(145, 35, 255, 255)));


    public Map<EntityEnderCrystal, BlockPos> explodedCrystals = new HashMap<EntityEnderCrystal, BlockPos>();
    public BlockPos crystalPos = null;
    public int aliveTicks = 0;
    public double speed = 0.0;
    public Timer timer = new Timer();


    public static HashMap<UUID, Thingering> thingers;

    public CrystalSpawns() {
        super("CrystalSpawns", "CrystalSpawns", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static CrystalSpawns getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrystalSpawns();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.explodedCrystals.clear();
    }


    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        for (Entity entity : CrystalSpawns.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || thingers.containsKey(entity.getUniqueID())) continue;
            thingers.put(entity.getUniqueID(), new Thingering(this, entity));
            CrystalSpawns.thingers.get(entity.getUniqueID()).starTime = System.currentTimeMillis();
            if (CrystalSpawns.fullNullCheck()) {
                return;
            }
            ++this.aliveTicks;
            if (this.timer.passedMs(5L)) {
                this.speed += 0.5;
                this.timer.reset();
            }
            if (this.speed > (double)this.riseSpeed.getValue().intValue()) {
                this.speed = 0.0;
                this.explodedCrystals.clear();
            }
        }
    }


    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        try {
            for (Entity crystal : CrystalSpawns.mc.world.loadedEntityList) {
                if (!(crystal instanceof EntityEnderCrystal) || !(event.getPacket() instanceof SPacketExplosion)) continue;
                this.crystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
                this.explodedCrystals.put((EntityEnderCrystal)crystal, this.crystalPos);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }


    @Override
    public void onRender3D(Render3DEvent eventRender3D) {
        if (this.Mode.getValue() == RenderMode.EUROPA) {
            if (CrystalSpawns.mc.player == null || CrystalSpawns.mc.world == null) {
                return;
            }
            for (Map.Entry<UUID, Thingering> entry : thingers.entrySet()) {
                if (System.currentTimeMillis() - entry.getValue().starTime >= ((long) 180056445 ^ 0xABB74A1L)) continue;
                float opacity = Float.intBitsToFloat(Float.floatToIntBits(1.2886874E38f) ^ 0x7EC1E66F);
                long time = System.currentTimeMillis();
                long duration = time - entry.getValue().starTime;
                if (duration < ((long) -1747803867 ^ 0xFFFFFFFF97D2A4F9L)) {
                    opacity = Float.intBitsToFloat(Float.floatToIntBits(13.7902155f) ^ 0x7EDCA4B9) - (float) duration / Float.intBitsToFloat(Float.floatToIntBits(6.1687006E-4f) ^ 0x7E9A3573);
                }
                CrystalSpawns.drawCircle(entry.getValue().entity, eventRender3D.getPartialTicks(), Double.longBitsToDouble(Double.doubleToLongBits(205.3116845075892) ^ 0x7F89A9F951C9D87FL), (float) (System.currentTimeMillis() - entry.getValue().starTime) / Float.intBitsToFloat(Float.floatToIntBits(0.025765074f) ^ 0x7E1B1147), opacity);
            }

        }
        if (this.Mode.getValue() == RenderMode.OCTO) {
            if (!this.explodedCrystals.isEmpty()) {
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.7f, this.crystalPos.getZ(), 0.6f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha() - 60));
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.6f, this.crystalPos.getZ(), 0.5f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha() - 50));
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.5f, this.crystalPos.getZ(), 0.4f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha() - 40));
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.4f, this.crystalPos.getZ(), 0.3f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha() - 30));
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.3f, this.crystalPos.getZ(), 0.2f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha() - 20));
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.2f, this.crystalPos.getZ(), 0.1f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha() - 10));
                RenderUtil.drawCircle(this.crystalPos.getX(), (float) this.crystalPos.getY() + (float) this.speed / 3.0f + 0.1f, this.crystalPos.getZ(), 0.0f + this.increase.getValue().floatValue(), new Color(this.Color.getValue().getRed(), this.Color.getValue().getGreen(), this.Color.getValue().getBlue(), this.Color.getValue().getAlpha()));
            }
        }
    }

    public static void drawCircle(final Entity entity, final float partialTicks, final double rad,
                                  final float plusY, final float alpha) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(Float.intBitsToFloat(Float.floatToIntBits(0.8191538f) ^ 0x7F11B410));
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - CrystalSpawns.mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - CrystalSpawns.mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - CrystalSpawns.mc.getRenderManager().viewerPosZ;
        final double pix2 = Double.longBitsToDouble(Double.doubleToLongBits(0.12418750450734782) ^ 0x7FA6EB3BC22A7D2FL);
        for (int i = 0; i <= 90; ++i) {
            if (CrystalSpawns.getInstance().Rainbow.getValue()) {
                GL11.glColor4f((ColorUtil.rainbow(50).getRed() / 255.0f), (ColorUtil.rainbow(50).getGreen() / 255.0f), (ColorUtil.rainbow(50).getBlue() / 255.0f), alpha);
            } else {
                GL11.glColor4f((CrystalSpawns.getInstance().Color.getValue().getRed() / 255.0f), (CrystalSpawns.getInstance().Color.getValue().getGreen() / 255.0f), (CrystalSpawns.getInstance().Color.getValue().getBlue() / 255.0f), alpha);
            }
            GL11.glVertex3d(x + rad * Math.cos(i * Double.longBitsToDouble(Double.doubleToLongBits(0.038923223119235344) ^ 0x7FBACC45F0F011C7L) / Double.longBitsToDouble(Double.doubleToLongBits(0.010043755046771538) ^ 0x7FC211D1FBA3AC6BL)), y + plusY / Float.intBitsToFloat(Float.floatToIntBits(0.13022153f) ^ 0x7F2558CB), z + rad * Math.sin(i * Double.longBitsToDouble(Double.doubleToLongBits(0.012655047216797511) ^ 0x7F90CB18FB234FBFL) / Double.longBitsToDouble(Double.doubleToLongBits(0.00992417958121009) ^ 0x7FC2D320D5ED6BD3L)));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }


    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
    }

    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
    }

    static {
        thingers = new HashMap();
    }

    public class Thingering {
        public Entity entity;
        public long starTime;
        public CrystalSpawns this$0;

        public Thingering(CrystalSpawns this$0, Entity entity) {
            this.this$0 = this$0;
            this.entity = entity;
            this.starTime = (long) 1417733199 ^ 0x5480E44FL;
        }
    }


    public enum RenderMode {
       EUROPA,
        OCTO
    }
}
