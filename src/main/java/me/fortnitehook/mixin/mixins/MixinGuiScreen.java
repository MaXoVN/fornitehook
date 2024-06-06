package me.fortnitehook.mixin.mixins;

import me.fortnitehook.features.modules.render.NoRender;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {GuiScreen.class})
public class MixinGuiScreen
        extends Gui {

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    public void drawDefaultBackground(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().containerBackground.getValue().booleanValue()) {
            info.cancel();
        }
    }
}


