package me.fortnitehook.mixin.mixins;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import me.fortnitehook.features.modules.render.NoRender;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = {World.class})
public class MixinWorld {
    @Redirect(method = {"getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"))
    public <T extends Entity> void getEntitiesOfTypeWithinAABBHook(Chunk chunk, Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> filter) {
        try {
            chunk.getEntitiesOfTypeWithinAABB(entityClass, aabb, listToFill, filter);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    @Inject(method = {"checkLightFor"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void updateLightmapHook(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (lightType == EnumSkyBlock.SKY && NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.WORLD || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}

