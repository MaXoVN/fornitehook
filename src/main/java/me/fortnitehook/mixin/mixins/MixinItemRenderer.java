package me.fortnitehook.mixin.mixins;

import me.fortnitehook.features.modules.render.NoRender;
import me.fortnitehook.features.modules.render.ViewModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ItemRenderer.class})
public abstract class MixinItemRenderer {
    private final boolean injection = true;

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);


    @Inject(method = {"renderFireInFirstPerson"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue().booleanValue()) {
            info.cancel();
        }
    }


    @Inject(method = {"renderSuffocationOverlay"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue().booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderItemSide", at = @At("HEAD"))
    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ViewModel.INSTANCE.isEnabled()) {
            GlStateManager.scale(ViewModel.INSTANCE.scaleX.getValue() / 100F, ViewModel.INSTANCE.scaleY.getValue() / 100F, ViewModel.INSTANCE.scaleZ.getValue() / 100F);
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate(ViewModel.INSTANCE.translateX.getValue() / 100F, ViewModel.INSTANCE.translateY.getValue() / 100F, ViewModel.INSTANCE.translateZ.getValue() / 100F);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateZ.getValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(-ViewModel.INSTANCE.translateX.getValue() / 100F, ViewModel.INSTANCE.translateY.getValue() / 100F, ViewModel.INSTANCE.translateZ.getValue() / 100F);
                GlStateManager.rotate(-ViewModel.INSTANCE.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateZ.getValue(), 0, 0, 1);
            }
        }
    }
}

