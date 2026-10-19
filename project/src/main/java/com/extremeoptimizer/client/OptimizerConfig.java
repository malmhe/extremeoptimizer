package com.extremeoptimizer.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OptimizerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("extremeoptimizer.json");

    public boolean entityCullingEnabled = true;
    public boolean autoOptimizeEnabled = true;
    public boolean aggressiveCulling = true;
    public int entityCullDistance = 48;
    public int maxEntityRenderDistance = 64;
    public boolean disableVsync = false;
    public int targetFps = 260;
    public boolean reduceParticles = true;
    public boolean reduceClouds = true;
    public boolean disableFog = false;
    public int renderDistanceBoost = 0;
    public boolean lowCpuMode = false;

    public static OptimizerConfig load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, OptimizerConfig.class);
            }
        } catch (IOException e) {
            ExtremeOptimizerClient.LOGGER.warn("Не удалось прочитать конфиг, использую дефолт", e);
        }
        return new OptimizerConfig();
    }

    public void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            ExtremeOptimizerClient.LOGGER.error("Не удалось сохранить конфиг", e);
        }
    }

    public void applyAutoOptimization() {
        HardwareProfile hw = HardwareScanner.scan();
        ExtremeOptimizerClient.LOGGER.info("Авто-оптимизация под профиль: {}", hw.getProfileName());

        if (hw.cpuCores <= 4) {
            lowCpuMode = true;
            entityCullDistance = 32;
            maxEntityRenderDistance = 48;
            reduceParticles = true;
            reduceClouds = true;
            targetFps = 120;
        } else if (hw.cpuCores <= 8) {
            lowCpuMode = false;
            entityCullDistance = 48;
            maxEntityRenderDistance = 64;
            reduceParticles = true;
            reduceClouds = true;
            targetFps = 240;
        } else {
            lowCpuMode = false;
            entityCullDistance = 64;
            maxEntityRenderDistance = 96;
            reduceParticles = false;
            reduceClouds = false;
            targetFps = 360;
        }

        if (hw.ramMB < 4096) {
            renderDistanceBoost = -2;
        } else if (hw.ramMB >= 16384) {
            renderDistanceBoost = 2;
        } else {
            renderDistanceBoost = 0;
        }

        save();
    }
}
