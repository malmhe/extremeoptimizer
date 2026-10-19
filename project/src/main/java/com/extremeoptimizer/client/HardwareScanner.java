package com.extremeoptimizer.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class HardwareScanner {

    public static int getCpuCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static long getTotalRamMB() {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        if (os instanceof com.sun.management.OperatingSystemMXBean sun) {
            return sun.getTotalMemorySize() / (1024 * 1024);
        }
        return Runtime.getRuntime().maxMemory() / (1024 * 1024);
    }

    public static String getCpuName() {
        try {
            OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
            return os.getName() + " " + os.getArch() + " (" + getCpuCores() + " потоков)";
        } catch (Exception e) {
            return "Unknown CPU";
        }
    }

    public static String getGpuInfo() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            long window = client.getWindow().getHandle();
            String vendor = RenderSystem.getBackendDescription();
            return vendor != null ? vendor : "Unknown GPU";
        } catch (Throwable t) {
            return "Unknown GPU";
        }
    }

    public static int estimateGpuScore() {
        try {
            String gpu = getGpuInfo().toLowerCase();
            if (gpu.contains("rtx") || gpu.contains("rx 6") || gpu.contains("rx 7")) return 90;
            if (gpu.contains("gtx") || gpu.contains("rx 5")) return 60;
            if (gpu.contains("intel") || gpu.contains("uhd")) return 30;
            return 40;
        } catch (Exception e) {
            return 40;
        }
    }

    public static HardwareProfile scan() {
        ExtremeOptimizerClient.LOGGER.info("Сканирую железо...");
        int cores = getCpuCores();
        long ram = getTotalRamMB();
        String cpuName = getCpuName();
        String gpu = getGpuInfo();
        int score = estimateGpuScore();
        return new HardwareProfile(cores, ram, cpuName, gpu, score);
    }

    public static String buildReport() {
        HardwareProfile hw = scan();
        return String.format(
                """
                === Extreme Optimizer — Отчёт по железо ===
                Профиль: %s
                CPU: %s
                Ядра/потоки: %d
                ОЗУ: %d МБ
                GPU: %s
                Оценка GPU: %d/100
                Рекомендованный FPS-таргет: %s
                """,
                hw.getProfileName(),
                hw.cpuName,
                hw.cpuCores,
                hw.ramMB,
                hw.gpuInfo,
                hw.gpuEstimateScore,
                hw.cpuCores >= 8 ? "240-360" : hw.cpuCores >= 4 ? "120-240" : "60-120"
        );
    }
}
