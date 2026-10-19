package com.extremeoptimizer.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtremeOptimizerClient implements ClientModInitializer {
    public static final String MOD_ID = "extremeoptimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("ExtremeOptimizer");

    private static OptimizerConfig config;

    @Override
    public void onInitializeClient() {
        config = OptimizerConfig.load();
        LOGGER.info("Extreme Optimizer загружен. Entity culling: {}", config.entityCullingEnabled);
        LOGGER.info("Железо: {} ядер CPU, {} МБ ОЗУ", HardwareScanner.getCpuCores(), HardwareScanner.getTotalRamMB());
    }

    public static OptimizerConfig getConfig() {
        return config;
    }

    public static void saveConfig() {
        config.save();
    }
}
