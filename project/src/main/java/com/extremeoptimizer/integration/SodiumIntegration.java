package com.extremeoptimizer.integration;

import com.extremeoptimizer.client.ExtremeOptimizerClient;
import com.extremeoptimizer.client.HardwareScanner;
import com.extremeoptimizer.client.OptimizerConfig;
import me.jellysquid.mods.sodium.client.gui.VanillaOptionAccess;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SodiumIntegration {

    public static OptionPage createOptimizerPage() {
        List<OptionGroup> groups = new ArrayList<>();
        OptimizerConfig cfg = ExtremeOptimizerClient.getConfig();

        OptionStorage<OptimizerConfig> storage = new OptimizerStorage();

        Option<Boolean> autoOptimize = Option.createBuilder(Boolean.class)
                .name(Text.literal("Авто-оптимизация"))
                .tooltip(Text.literal("Сканирует железо и ставит оптимальные настройки"))
                .control(TickBoxControl::new)
                .binding(true, () -> cfg.autoOptimizeEnabled, v -> cfg.autoOptimizeEnabled = v)
                .build();

        Option<Boolean> entityCulling = Option.createBuilder(Boolean.class)
                .name(Text.literal("Entity Culling (вкл/выкл)"))
                .tooltip(Text.literal("Не рендерить сущности вне экрана и далеко от игрока"))
                .control(TickBoxControl::new)
                .binding(true, () -> cfg.entityCullingEnabled, v -> cfg.entityCullingEnabled = v)
                .build();

        Option<Boolean> aggressiveCull = Option.createBuilder(Boolean.class)
                .name(Text.literal("Агрессивный culling"))
                .tooltip(Text.literal("Сильнее режет дальность рендера сущностей"))
                .control(TickBoxControl::new)
                .binding(true, () -> cfg.aggressiveCulling, v -> cfg.aggressiveCulling = v)
                .build();

        Option<Integer> cullDist = Option.createBuilder(Integer.class)
                .name(Text.literal("Дистанция culling"))
                .tooltip(Text.literal("Начиная с какого расстояния сущности не рендерятся"))
                .control(opt -> new SliderControl(opt, 16, 128, 1))
                .binding(48, () -> cfg.entityCullDistance, v -> cfg.entityCullDistance = v)
                .build();

        Option<Integer> targetFps = Option.createBuilder(Integer.class)
                .name(Text.literal("Целевой FPS"))
                .tooltip(Text.literal("Лимит кадров (0 = без лимита)"))
                .control(opt -> new SliderControl(opt, 60, 1000, 10))
                .binding(260, () -> cfg.targetFps, v -> cfg.targetFps = v)
                .build();

        Option<Boolean> reduceParticles = Option.createBuilder(Boolean.class)
                .name(Text.literal("Сокращать частицы"))
                .control(TickBoxControl::new)
                .binding(true, () -> cfg.reduceParticles, v -> cfg.reduceParticles = v)
                .build();

        Option<Boolean> reduceClouds = Option.createBuilder(Boolean.class)
                .name(Text.literal("Убрать облака"))
                .control(TickBoxControl::new)
                .binding(true, () -> cfg.reduceClouds, v -> cfg.reduceClouds = v)
                .build();

        groups.add(OptionGroup.createBuilder().add(autoOptimize).build());
        groups.add(OptionGroup.createBuilder().add(entityCulling).add(aggressiveCull).add(cullDist).build());
        groups.add(OptionGroup.createBuilder().add(targetFps).build());
        groups.add(OptionGroup.createBuilder().add(reduceParticles).add(reduceClouds).build());

        return new OptionPage(
                Text.literal("Extreme Optimizer"),
                Text.literal("Жесткая оптимизация FPS"),
                groups
        );
    }

    public static void runAutoOptimize() {
        ExtremeOptimizerClient.LOGGER.info("Запущен процесс авто-оптимизации");
        ExtremeOptimizerClient.getConfig().applyAutoOptimization();
        ExtremeOptimizerClient.LOGGER.info(HardwareScanner.buildReport());
    }
}
