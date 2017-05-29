package edu.agh.io.industryOptimizer.messaging;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CallbacksUtilityImpl implements CallbacksUtility {
    private final Map<Object, Map<Object, List<MessageCallback>>> callbacksMap = new HashMap<>();
    private final Map<Object, List<MessageCallback>> allStatesCallbacks = new HashMap<>();

    @Override
    public void executeCallbacks(Object state, Object messageType, Object message) {
        callbacksMap.getOrDefault(state, new HashMap<>())
                .getOrDefault(messageType, new LinkedList<>())
                .forEach(callback -> {
                    callback.messageReceived(message);
                });

        allStatesCallbacks.getOrDefault(messageType, new LinkedList<>())
                .forEach(callback ->
                        callback.messageReceived(message)

                );
    }

    @Override
    public void addCallback(Object state, Object messageType, MessageCallback callback) {
        callbacksMap
                .computeIfAbsent(
                        state,
                        key -> new HashMap<>())
                .computeIfAbsent(
                        messageType,
                        key -> new LinkedList<>())
                .add(callback);
    }

    @Override
    public void addCallbackAllStates(Object messageType, MessageCallback callback) {
        allStatesCallbacks.computeIfAbsent(messageType, key ->
                new LinkedList<>()
        ).add(callback);
    }

    @Override
    public void removeCallback(MessageCallback callback) {
        allStatesCallbacks.values().forEach(list ->
                list.remove(callback)
        );
        callbacksMap.values().forEach(map ->
                map.values().forEach(list ->
                        list.remove(callback)
                )
        );
    }
}
