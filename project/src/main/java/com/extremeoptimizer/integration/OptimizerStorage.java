package com.extremeoptimizer.integration;

import com.extremeoptimizer.client.ExtremeOptimizerClient;
import com.extremeoptimizer.client.OptimizerConfig;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;

public class OptimizerStorage implements OptionStorage<OptimizerConfig> {

    @Override
    public OptimizerConfig getData() {
        return ExtremeOptimizerClient.getConfig();
    }

    @Override
    public void save() {
        ExtremeOptimizerClient.saveConfig();
    }
}
