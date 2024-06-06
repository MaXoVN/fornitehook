package me.fortnitehook.mixin.mixins;

import me.fortnitehook.event.events.PerspectiveEvent;
import me.fortnitehook.event.events.PreRenderEvent;
import me.fortnitehook.features.modules.render.NoRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.vecmath.Vector3f;

@Mixin(value = {EntityRenderer.class})
public abstract class MixinEntityRenderer {
    @Shadow
    public ItemStack itemActivationItem;
    @Shadow
    public Minecraft mc;
    @Shadow
    @Final
    private int[] lightmapColors;
    private boolean injection = true;

    @Shadow
    public abstract void getMouseOver(float var1);

    @Inject(method = {"renderItemActivation"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderItemActivationHook(CallbackInfo info) {
        if (this.itemActivationItem != null && NoRender.getInstance().isOn() && NoRender.getInstance().totemPops.getValue().booleanValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }

    @Inject(method = {"updateLightmap"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }
    }


    private int[] toRGBAArray(int colorBuffer) {
        return new int[]{colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF};
    }

    private Vector3f mix(Vector3f first, Vector3f second, float factor) {
        return new Vector3f(first.x * (1.0f - factor) + second.x * factor, first.y * (1.0f - factor) + second.y * factor, first.z * (1.0f - factor) + first.z * factor);
    }


    @Redirect(method = {"setupCameraTransform"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"))
    public float prevTimeInPortalHook(EntityPlayerSP entityPlayerSP) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().nausea.getValue().booleanValue()) {
            return -3.4028235E38f;
        }
        return entityPlayerSP.prevTimeInPortal;
    }

    @Redirect(method = {"setupCameraTransform"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onSetupCameraTransform(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent((float) this.mc.displayWidth / (float) this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post(perspectiveEvent);
        Project.gluPerspective(f, perspectiveEvent.getAspect(), f3, f4);
    }

    @Redirect(method = {"renderWorldPass"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderWorldPass(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent((float) this.mc.displayWidth / (float) this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post(perspectiveEvent);
        Project.gluPerspective(f, perspectiveEvent.getAspect(), f3, f4);
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;clear(I)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        if (Display.isActive() || Display.isVisible()) {
            PreRenderEvent render3dEvent = new PreRenderEvent(partialTicks);
            MinecraftForge.EVENT_BUS.post(render3dEvent);
        }
    }

    @Redirect(method = {"renderCloudsCheck"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderCloudsCheck(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent((float) this.mc.displayWidth / (float) this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post(perspectiveEvent);
        Project.gluPerspective(f, perspectiveEvent.getAspect(), f3, f4);
    }


    @Inject(method = {"setupFog"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void setupFogHook(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.NOFOG) {
            info.cancel();
        }
    }

    @Redirect(method = {"setupFog"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getBlockStateAtEntityViewpointHook(World worldIn, Entity entityIn, float p_186703_2_) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.AIR) {
            return Blocks.AIR.defaultBlockState;
        }
        return ActiveRenderInfo.getBlockStateAtEntityViewpoint(worldIn, entityIn, p_186703_2_);
    }

    @Inject(method = {"hurtCameraEffect"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().hurtcam.getValue().booleanValue()) {
            info.cancel();
        }
    }
}



