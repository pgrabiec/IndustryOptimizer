package edu.agh.io.industryOptimizer.messaging;

import java.util.*;

public class CallbacksUtilityImpl implements CallbacksUtility {
    private final Map<Object, Map<Object, List<MessageCallback>>> callbacksMap = new HashMap<>();
    private final Map<Object, List<MessageCallback>> allStatesCallbacks = new HashMap<>();

    private final Map<MessageCallback, Set<Object>> exceptStates = new HashMap<>();
    private final Set<MessageCallback> allExceptStatesCallbacks = new HashSet<>();

    @Override
    public void executeCallbacks(Object state, Object messageType, Object message) {
        Set<MessageCallback> executed = new HashSet<>();

        callbacksMap.getOrDefault(state, new HashMap<>())
                .getOrDefault(messageType, new LinkedList<>())
                .forEach(callback -> {
                    if (!executed.contains(callback)) {
                        callback.messageReceived(message);
                        executed.add(callback);
                    }
                });

        allStatesCallbacks.getOrDefault(messageType, new LinkedList<>())
                .forEach(callback -> {
                            if (!executed.contains(callback)) {
                                callback.messageReceived(message);
                                executed.add(callback);
                            }
                });

        allExceptStatesCallbacks.forEach(callback -> {
            boolean stateInExcept = exceptStates
                    .getOrDefault(callback, new HashSet<>())
                    .contains(state);
            if (!stateInExcept) {
                if (!executed.contains(callback)) {
                    callback.messageReceived(message);
                    executed.add(callback);
                }
            }
        });
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

        allExceptStatesCallbacks.remove(callback);
        exceptStates.remove(callback);
    }

    @Override
    public void addCallbackAllStatesExcept(Object MessageType, MessageCallback callback, Collection<Object> exceptStates) {
        allExceptStatesCallbacks.add(callback);
        this.exceptStates.computeIfAbsent(callback, key -> new HashSet<>())
                .addAll(exceptStates);
    }
}
