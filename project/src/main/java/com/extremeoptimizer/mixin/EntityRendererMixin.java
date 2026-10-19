package com.extremeoptimizer.mixin;

import com.extremeoptimizer.client.EntityCuller;
import com.extremeoptimizer.client.ExtremeOptimizerClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void onRender(
            E entity,
            double x,
            double y,
            double z,
            float tickDelta,
            float yaw,
            CallbackInfo ci
    ) {
        try {
            if (EntityCuller.shouldCull(entity)) {
                ci.cancel();
            }
        } catch (Throwable t) {
            ExtremeOptimizerClient.LOGGER.debug("Cull check error", t);
        }
    }
}
