package edu.agh.io.industryOptimizer.messaging.messages.util;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ConfigApplierImpl<C, V> implements ConfigApplier<C, V> {
    private final Map<Object, List<Consumer<V>>> callbacksMap = new HashMap<>();
    private Mapper<C, V> argumentMapper;
    private Mapper<C, Object> typeMapper;

    @Override
    public ConfigApplier<C, V> callback(@NotNull Object type, @NotNull Consumer<V> callback) {
        assert type != null;
        assert callback != null;

        callbacksMap.computeIfAbsent(type, o -> new LinkedList<>())
                .add(callback);
        return this;
    }



    @Override
    public ConfigApplier<C, V> argument(Mapper<C, V> argumentMapper) {
        assert argumentMapper != null;

        this.argumentMapper = argumentMapper;

        return this;
    }

    @Override
    public ConfigApplier<C, V> type(Mapper<C, Object> typeMapper) {
        assert typeMapper != null;

        this.typeMapper = typeMapper;

        return this;
    }

    @Override
    public void execute(C config) {
        if (argumentMapper == null) {
            throw new IllegalStateException("Attempt to execute config applier without setting " +
                    "argument mapper. Try supplying one by .argument(Mapper) method");
        }

        if (typeMapper == null) {
            throw new IllegalStateException("Attempt to execute config applier without setting " +
                    "type mapper. Try supplying one by .type(Mapper) method");
        }

        callbacksMap
                .getOrDefault(typeMapper.map(config), Collections.emptyList())
                .forEach(consumer ->
                        consumer.accept(
                                argumentMapper.map(config)
                        ));
    }
}
