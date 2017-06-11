package edu.agh.io.industryOptimizer.messaging.messages.util;

import java.util.function.Consumer;

public interface ConfigApplier<C, V> {
    /**
     * Set callbacks for different config types
     */
    public ConfigApplier<C, V> callback(Object type, Consumer<V> callback);


    /**
     * Decide what to pass to callback
     * */
    public ConfigApplier<C, V> argument(Mapper<C, V> argumentMapper);

    /**
     * Decide what to pass to callback
     * */
    public ConfigApplier<C, V> type(Mapper<C, Object> typeMapper);

    /**
     * Execute callbacks against the config
     * */
    public void execute(C config);
}
