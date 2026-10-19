package com.extremeoptimizer.mixin;

import com.extremeoptimizer.client.EntityCuller;
import com.extremeoptimizer.client.ExtremeOptimizerClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "renderEntities", at = @At("HEAD"), cancellable = true)
    private void preRenderEntities(CallbackInfo ci) {
        if (ExtremeOptimizerClient.getConfig() != null
                && ExtremeOptimizerClient.getConfig().entityCullingEnabled) {
            // hint-флаг: фильтрация идёт в EntityRendererMixin
        }
    }
}
