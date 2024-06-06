package me.fortnitehook.features.modules.render;

import me.fortnitehook.features.command.Command;
import me.fortnitehook.features.setting.*;
import me.fortnitehook.util.*;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.setting.Setting;
import me.fortnitehook.util.EntityUtil;
import me.fortnitehook.util.VectorUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AmongUsESP extends Module
{
    private final Setting<CachedImage> imageUrl;
    private final Setting<Boolean> noRenderPlayers = this.register(new Setting<Boolean>("NoRenderPlayers", false));
    private ResourceLocation femboy;
    private final ICamera camera;

    public static final EventBus EVENT_BUS = MinecraftForge.EVENT_BUS;

    public AmongUsESP() {
        super("AmongUsESP", "draws cute amongus on people", Module.Category.RENDER, false, false, false);
        this.imageUrl = this.register(new Setting<CachedImage>("Image", CachedImage.AmongUsDripPurple));
        this.camera = new Frustum();
        this.onLoad();
    }

    @Override
    public void onEnable() {
        AmongUsESP.EVENT_BUS.register(this);
        Command.sendMessage("Toggle on and off to reload the image");
        this.femboy = null;
        this.onLoad();
    }

    @Override
    public void onDisable() {
        AmongUsESP.EVENT_BUS.unregister(this);
    }

    private <T> BufferedImage getImage(final T source, final ThrowingFunction<T, BufferedImage> readFunction) {
        try {
            return readFunction.apply(source);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean shouldDraw(final EntityLivingBase entity) {
        return !entity.equals(AmongUsESP.mc.player) && entity.getHealth() > 0.0f && EntityUtil.isPlayer(entity);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (this.femboy == null) {
            return;
        }
        final double d3 = AmongUsESP.mc.player.lastTickPosX + (AmongUsESP.mc.player.posX - AmongUsESP.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = AmongUsESP.mc.player.lastTickPosY + (AmongUsESP.mc.player.posY - AmongUsESP.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = AmongUsESP.mc.player.lastTickPosZ + (AmongUsESP.mc.player.posZ - AmongUsESP.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        final List<EntityPlayer> players = new ArrayList<EntityPlayer>(AmongUsESP.mc.world.playerEntities);
        players.sort(Comparator.comparing(entityPlayer -> AmongUsESP.mc.player.getDistance((Entity)entityPlayer)).reversed());
        for (final EntityPlayer player : players) {
            if (player != AmongUsESP.mc.getRenderViewEntity() && player.isEntityAlive() && this.camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) {
                final EntityLivingBase living = player;
                final Vec3d bottomVec = EntityUtil.getInterpolatedPos(living, event.getPartialTicks());
                final Vec3d topVec = bottomVec.add(new Vec3d(0.0, player.getRenderBoundingBox().maxY - player.posY, 0.0));
                final VectorUtils.ScreenPos top = VectorUtils._toScreen(topVec.x, topVec.y, topVec.z);
                final VectorUtils.ScreenPos bot = VectorUtils._toScreen(bottomVec.x, bottomVec.y, bottomVec.z);
                if (!top.isVisible && !bot.isVisible) {
                    continue;
                }
                final int height;
                final int width = height = bot.y - top.y;
                final int x = (int)(top.x - width / 1.8);
                final int y = top.y;
                AmongUsESP.mc.renderEngine.bindTexture(this.femboy);
                GlStateManager.color(255.0f, 255.0f, 255.0f);
                Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float)width, (float)height);
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Pre event) {
        if (this.noRenderPlayers.getValue() && !event.getEntity().equals(AmongUsESP.mc.player)) {
            event.setCanceled(true);
        }
    }


    public void onLoad() {
        BufferedImage image = null;
        try {
            if (this.getFile(this.imageUrl.getValue().getName()) != null) {
                image = this.getImage(this.getFile(this.imageUrl.getValue().getName()), ImageIO::read);
            }
            if (image == null) {
                Command.sendMessage("Failed to load image");
            }
            else {
                final DynamicTexture dynamicTexture = new DynamicTexture(image);
                dynamicTexture.loadTexture(AmongUsESP.mc.getResourceManager());
                this.femboy = AmongUsESP.mc.getTextureManager().getDynamicTextureLocation("Bucket" + this.imageUrl.getValue().name(), dynamicTexture);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream getFile(final String string) {
        return AmongUsESP.class.getResourceAsStream(string);
    }

    private enum CachedImage
    {
        Peter_Griffin("/images/Peter_Griffin.png"),
        AmongUsDripPurple("/images/amongusdrippurple.png");


        String name;

        CachedImage(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R>
    {
        R apply(final T p0) throws IOException;
    }



}