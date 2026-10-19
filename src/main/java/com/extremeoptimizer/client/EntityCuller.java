package com.extremeoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public final class EntityCuller {
    private EntityCuller() {}

    public static boolean shouldCull(Entity entity) {
        OptimizerConfig cfg = ExtremeOptimizerClient.getConfig();
        if (!cfg.entityCullingEnabled) return false;
        if (entity == null) return false;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return false;

        if (entity == client.player || entity.isPlayer()) {
            return false;
        }

        if (entity.hasVehicle() || client.player.hasVehicle()) {
            return false;
        }

        Vec3d playerPos = client.player.getPos();
        Vec3d entPos = entity.getPos();
        double dx = playerPos.x - entPos.x;
        double dz = playerPos.z - entPos.z;
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);

        int maxDist = cfg.maxEntityRenderDistance;
        if (cfg.aggressiveCulling) {
            maxDist = cfg.entityCullDistance;
        }

        if (horizontalDist > maxDist) {
            return true;
        }

        if (cfg.aggressiveCulling && !isInFrustum(entity, client)) {
            return true;
        }

        return false;
    }

    private static boolean isInFrustum(Entity entity, MinecraftClient client) {
        try {
            return client.getEntityRenderDispatcher().shouldRender(
                    entity,
                    client.gameRenderer.getCamera().getFrustum(),
                    client.gameRenderer.getCamera().getPos().x,
                    client.gameRenderer.getCamera().getPos().y,
                    client.gameRenderer.getCamera().getPos().z
            );
        } catch (Throwable t) {
            return true;
        }
    }
}
