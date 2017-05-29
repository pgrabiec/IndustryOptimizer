package edu.agh.io.industryOptimizer.messaging;

import java.util.*;

public class CallbacksUtility {
    private final Map<Object, Map<Object, List<Runnable>>> callbacksMap = new HashMap<>();

    public Collection<Runnable> getCallback(Object state, Object messageType) {
        return callbacksMap
                .computeIfAbsent(
                        state,
                        key -> new HashMap<>())
                .getOrDefault(
                        messageType,
                        new LinkedList<>()
                );
    }

    public void addCallback(Object state, Object messageType, Runnable callback) {
        callbacksMap
                .computeIfAbsent(
                        state,
                        key -> new HashMap<>())
                .getOrDefault(
                        messageType,
                        new LinkedList<>())
                .add(callback);
    }
}
