package me.fortnitehook.features.modules.client.TargetHud;


import com.mojang.realmsclient.gui.ChatFormatting;
import me.fortnitehook.features.command.Command;
import me.fortnitehook.features.modules.combat.CrystalAura;
import me.fortnitehook.util.*;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.combat.Aura;
import me.fortnitehook.features.modules.misc.PopCounter;



import me.fortnitehook.OyVey;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.event.events.Render2DEvent;
import me.fortnitehook.util.Timer;
import me.fortnitehook.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.*;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;



public class TargetHud extends Module {
    private static TargetHud INSTANCE = new TargetHud();



    public static TargetHud getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TargetHud();
        }
        return INSTANCE;
    }



    private final Setting<Integer> Xpos = this.register(new Setting<Integer>("XPosition", 500, -100, 850));
    private final Setting<Integer> Ypos = this.register(new Setting<Integer>("YPosition", 300, 0, 512));
    private final Timer timer = new Timer();
    public Timer totemAnnounce = new Timer();
    public static HashMap<String, Integer> TotemPopContainer;





    private final java.util.ArrayList<Particles> particles = new java.util.ArrayList();
    private final Timer timeUtil = new Timer();
    public Setting<Integer> healthMixColorOneRed = this.register(new Setting<Object>("healthColor1Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorOneGreen = this.register(new Setting<Object>("healthColor1Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorOneBlue = this.register(new Setting<Object>("healthColor1Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorOneAlpha = this.register(new Setting<Object>("healthColor1Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorTwoRed = this.register(new Setting<Object>("healthColor2Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorTwoGreen = this.register(new Setting<Object>("healthColor2Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorTwoBlue = this.register(new Setting<Object>("healthColor2Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> healthMixColorTwoAlpha = this.register(new Setting<Object>("healthColor2Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> particleMixColorOneRed = this.register(new Setting<Object>("ParticleColor1Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> particleMixColorOneGreen = this.register(new Setting<Object>("ParticleColor1Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> particleMixColorOneBlue = this.register(new Setting<Object>("ParticleColor1Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> particleMixColorOneAlpha = this.register(new Setting<Object>("ParticleColor1Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));

    public Setting<Integer> particleMixColorTwoRed = this.register(new Setting<Object>("ParticleColor2Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));

    public Setting<Integer> particleMixColorTwoGreen = this.register(new Setting<Object>("ParticleColor2Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> particleMixColorTwoBlue = this.register(new Setting<Object>("ParticleColor2Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> particleMixColorTwoAlpha = this.register(new Setting<Object>("ParticleColor2Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    float ticks;
    int dragX, dragY = 0;
    boolean mousestate = false;
    float posX = 0;

    float posY = 0;

    private Entity target;
    private Entity lastTarget;
    private float displayHealth;
    private float health;
    private boolean sentParticles;
    private double scale  = 10;

    public TargetHud() {
        super("TargetHud", "TargetHud", Module.Category.CLIENT, true, false, false);
    }
    public static void renderPlayerModelTexture(final double x, final double y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight, final AbstractClientPlayer target) {
        final ResourceLocation skin = target.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        GL11.glEnable(GL11.GL_BLEND);
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
        GL11.glDisable(GL11.GL_BLEND);
    }


    @Override
    public void onRenderTick() {
        if (OyVey.moduleManager.isModuleEnabled(Aura.class) && Aura.INSTANCE.getTarget() != null) {
            target = Aura.INSTANCE.getTarget();
        } else if (OyVey.moduleManager.isModuleEnabled(CrystalAura.class) && CrystalAura.entityPlayer != null) {
            target = CrystalAura.entityPlayer;
        } else {
            target = null;
        }
    }






    public int normaliseX() {
        return (int) ((Mouse.getX() / 2f));
    }

    public int normaliseY() {
        ScaledResolution sr = new ScaledResolution(mc);
        return (((-Mouse.getY() + sr.getScaledHeight()) + sr.getScaledHeight()) / 2);
    }

    public boolean isHovering() {
        return normaliseX() > posX + 38 + 2 && normaliseX() < posX + 129 && normaliseY() > posY - 34 && normaliseY() < posY + 14;
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }

        EntityPlayer target = EntityUtil.getClosestEnemy(100.0);

        ScaledResolution sr = new ScaledResolution(mc);
        posX = Xpos.getValue();
        posY = Ypos.getValue();

        if (mc.currentScreen instanceof GuiChat) {
            target = mc.player;

            if (isHovering()) {
                if (Mouse.isButtonDown(0) && mousestate) {
//                    Xpos.setValue((normaliseX() - dragX) /  sr.getScaledWidth());
//                    Ypos.setValue((normaliseY() - dragY) / sr.getScaledHeight());
                    Xpos.setValue((normaliseX() - dragX));
                    Ypos.setValue((normaliseY() - dragY));
                }
            }
        } else if (target == mc.player) {
            target = null;
        }

        if (Mouse.isButtonDown(0) && isHovering()) {
            if (!mousestate) {
//                dragX = (int) (normaliseX() - (Xpos.getValue() * sr.getScaledWidth()));
//                dragY = (int) (normaliseY() - (Ypos.getValue() * sr.getScaledHeight()));
                dragX = normaliseX() - (Xpos.getValue());
                dragY = normaliseY() - (Ypos.getValue());
            }
            mousestate = true;
        } else {
            mousestate = false;
        }


        final float nameWidth = 38;

        if (timer.passedMs(9)) {
            if (target != null && (target.getDistance(mc.player) > 10 || mc.world.getEntityByID(Objects.requireNonNull(target).getEntityId()) == null)) {
                scale = Math.max(0, scale - timeUtil.getPassedTimeMs() / 8E+13 - (1 - scale) / 10);
                particles.clear();
                timer.reset();
            } else {
                scale = Math.min(1, scale + timeUtil.getPassedTimeMs() / 4E+14 + (1 - scale) / 10);
            }
        }

        if (target == null || !(target instanceof EntityPlayer)) {
            particles.clear();
            return;
        }

        if (scale == 0) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate((posX + 38 + 2 + 129 / 2f) * (1 - scale), (posY - 34 + 48 / 2f) * (1 - scale), 0);
        GlStateManager.scale(scale, scale, 0);

        final EntityPlayer en = (EntityPlayer) target;
        final double dist = mc.player.getDistance(target);

        final String name = target.getName();

        //Background
        //final ItemStack renderOffhand = ((EntityPlayer) target).getArmorVisibility().copy();

        Particles.roundedRect(posX + 38 + 2, posY - 34, 145, 48, 8, new Color(0, 0, 0, 110));
        //renderItemStack(renderOffhand, (int) posX + 38 + 2 + 200 - 22, (int) posY - 27);

        GlStateManager.popMatrix();

        final int scaleOffset = (int) (((EntityPlayer) target).hurtTime * 0.35f);

        for (final Particles p : particles) {
            if (p.opacity > 4) p.render2D();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((posX + 38 + 2 + 129 / 2f) * (1 - scale), (posY - 34 + 48 / 2f) * (1 - scale), 0);
        GlStateManager.scale(scale, scale, 0);

        if (target instanceof AbstractClientPlayer) {
            final double offset = -(((AbstractClientPlayer) target).hurtTime * 23);
            Particles.color(new Color(255, (int) (255 + offset), (int) (255 + offset)));
            try {
                renderPlayerModelTexture(posX + 38 + 6 + scaleOffset / 2f, posY - 34 + 5 + scaleOffset / 2f, 3, 3, 3, 3, 30 - scaleOffset, 30 - scaleOffset, 24, 24.5f, (AbstractClientPlayer) en);

            } catch (Exception ignored) {

            }
            renderPlayerModelTexture(posX + 38 + 6 + scaleOffset / 2f, posY - 34 + 5 + scaleOffset / 2f, 15, 3, 3, 3, 30 - scaleOffset, 30 - scaleOffset, 24, 24.5f, (AbstractClientPlayer) en);
            Particles.color(Color.WHITE);
        }

        final double fontHeight = 7;

        Util.fr.drawString("Distance: " + MathUtil.round(dist, 1), (int) (posX + 38 + 6 + 30 + 42), (int) (posY - 34 + 5 + 15 + 2), Color.WHITE.hashCode());
        Util.fr.drawString("Pops:" + " " + OyVey.totemPopManager.getTotemPops(target), (int) (posX + 38 + 6 + 30 + 3), (int) (posY - 34 + 5 + 15 + 2), Color.WHITE.hashCode());
        /* int iteration = 0;
        for (final ItemStack is : target.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final float x = posX + (9 - iteration) * 20 + 2 + 38 + 6 + 30 + 3;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, (int) x, 367);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(TargetHud.mc.fontRenderer, is, (int) x, 367, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
        }*/








        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        Particles.scissor(posX + 38 + 6 + 30 + 3, posY - 34 + 5 + 15 - fontHeight, 91, 30);
        Util.fr.drawString(name, (int) (posX + 38 + 6 + 30 + 3), (int) (posY - 34 + 5 + 15 - fontHeight), Color.WHITE.hashCode());

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        if (!String.valueOf(((EntityPlayer) target).getHealth()).equals("NaN"))
            health = Math.min(20, ((EntityPlayer) target).getHealth());

        if (String.valueOf(displayHealth).equals("NaN")) {
            displayHealth = (float) (Math.random() * 20);
        }

        if ((dist > 10) || target.isDead) {
            health = 0;
        }

        final int speed = 6;
        if (timer.passedMs(1000 / 60)) {
            displayHealth = (displayHealth * (speed - 1) + health) / speed;

            ticks += 0.1f;

            for (final Particles p : particles) {
                p.updatePosition();

                if (p.opacity < 1) particles.remove(p);
            }

            timer.reset();
        }



        float offset = 6;
        final float drawBarPosX = posX + nameWidth;

        if (displayHealth > 0.1)
            for (int i = 0; i < displayHealth * 4; i++) {
                int color = -1;
                Color healthMixColorOne = new Color ( this.healthMixColorOneRed.getValue ( ) , this.healthMixColorOneGreen.getValue ( ) , this.healthMixColorOneBlue.getValue ( ) , this.healthMixColorOneAlpha.getValue ( ) );
                Color healthMixColorTwo = new Color ( this.healthMixColorTwoRed.getValue ( ) , this.healthMixColorTwoGreen.getValue ( ) , this.healthMixColorTwoBlue.getValue ( ) , this.healthMixColorTwoAlpha.getValue ( ) );


                color = Particles.mixColors(healthMixColorOne, healthMixColorTwo, (Math.sin(ticks + posX * 0.4f + i * 0.6f / 14f) + 1) * 0.5f).hashCode();
                Gui.drawRect((int) (drawBarPosX + offset), (int) (posY + 5), (int) (drawBarPosX + 1 + offset * 1.25), (int) (posY + 10), color);
                offset += 1;
            }

        if ((((EntityPlayer) target).hurtTime == 9 && !sentParticles) || (lastTarget != null && ((EntityPlayer) lastTarget).hurtTime == 9 && !sentParticles)) {

            for (int i = 0; i <= 15; i++) {
                final Particles p = new Particles();
                final Color c;
                Color particleMixColorOne = new Color ( this.particleMixColorOneRed.getValue ( ) , this.particleMixColorOneGreen.getValue ( ) , this.particleMixColorOneBlue.getValue ( ) , this.particleMixColorOneAlpha.getValue ( ) );
                Color particleMixColorTwo = new Color ( this.particleMixColorTwoRed.getValue ( ) , this.particleMixColorTwoGreen.getValue ( ) , this.particleMixColorTwoBlue.getValue ( ) , this.particleMixColorTwoAlpha.getValue ( ) );


                c = Particles.mixColors(particleMixColorOne, particleMixColorTwo, (Math.sin(ticks + posX * 0.4f + i) + 1) * 0.5f);
                p.init(posX + 55, posY - 15, ((Math.random() - 0.5) * 2) * 1.4, ((Math.random() - 0.5) * 2) * 1.4, Math.random() * 4, c);
                particles.add(p);
            }

            sentParticles = true;
        }

        if (((EntityPlayer) target).hurtTime == 8) sentParticles = false;

        if (!(dist > 20 || target.isDead)) {
            Util.fr.drawString(MathUtil.round(displayHealth, 1) + "", (int) (drawBarPosX + 2 + offset * 1.25), (int) (posY + 2.5f), -1);
        }

        if (lastTarget != target) {
            lastTarget = target;
        }

        final java.util.ArrayList<Particles> removeList = new java.util.ArrayList<>();
        for (final Particles p : particles) {
            if (p.opacity <= 1) {
                removeList.add(p);
            }
        }

        for (final Particles p : removeList) {
            particles.remove(p);
        }
        GlStateManager.popMatrix();
        timeUtil.reset();
    }

    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(Util.fr, stack, x, y);
        mc.getRenderItem().zLevel = 0.0f;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

}