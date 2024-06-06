package me.fortnitehook.mixin.mixins.accessors;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={RenderManager.class})
public interface IRenderManager {
    @Accessor(value="renderPosX")
    double getRenderPosX();

    @Accessor(value="renderPosY")
    double getRenderPosY();

    @Accessor(value="renderPosZ")
    double getRenderPosZ();
}
