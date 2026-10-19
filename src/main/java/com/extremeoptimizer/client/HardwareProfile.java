package com.extremeoptimizer.client;

public class HardwareProfile {
    public final int cpuCores;
    public final long ramMB;
    public final String cpuName;
    public final String gpuInfo;
    public final int gpuEstimateScore;

    public HardwareProfile(int cpuCores, long ramMB, String cpuName, String gpuInfo, int gpuEstimateScore) {
        this.cpuCores = cpuCores;
        this.ramMB = ramMB;
        this.cpuName = cpuName;
        this.gpuInfo = gpuInfo;
        this.gpuEstimateScore = gpuEstimateScore;
    }

    public String getProfileName() {
        if (cpuCores >= 16 && ramMB >= 16384 && gpuEstimateScore >= 70) {
            return "ULTRA (16+ ядер, 16+ ГБ ОЗУ, мощная GPU)";
        } else if (cpuCores >= 8 && ramMB >= 8192) {
            return "HIGH (8+ ядер, 8+ ГБ ОЗУ)";
        } else if (cpuCores >= 4 && ramMB >= 4096) {
            return "MID (4+ ядер, 4+ ГБ ОЗУ)";
        }
        return "LOW (слабое железо — максимальная оптимизация)";
    }
}
