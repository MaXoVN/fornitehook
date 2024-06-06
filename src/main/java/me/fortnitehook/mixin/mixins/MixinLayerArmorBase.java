package me.fortnitehook.mixin.mixins;

import me.fortnitehook.OyVey;
import me.fortnitehook.features.modules.render.NoRender;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.fortnitehook.util.Util.mc;

@Mixin(value = {LayerArmorBase.class})
public class MixinLayerArmorBase {
    @Inject(method = {"doRenderLayer"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noArmor.getValue() == NoRender.NoArmor.ALL) {
            ci.cancel();
        }

        if (OyVey.moduleManager == null) {
            return;
        }
        if (OyVey.friendManager == null) {
            return;
        }
    }


    @Inject(method = {"renderArmorLayer"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn, CallbackInfo ci) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noArmor.getValue() == NoRender.NoArmor.HELMET && slotIn == EntityEquipmentSlot.HEAD) {
            ci.cancel();
        }
    }
}


