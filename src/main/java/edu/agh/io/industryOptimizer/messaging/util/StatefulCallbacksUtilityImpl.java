package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

import java.util.*;

public class StatefulCallbacksUtilityImpl extends CallbacksUtilityImpl implements StatefulCallbacksUtility {
    private final Map<Object, Map<Object, List<MessageCallback>>> callbacksMap = new HashMap<>();

    private final Set<MessageCallback> exceptStatesCallbacks = new HashSet<>();
    private final Map<MessageCallback, Set<Object>> exceptStates = new HashMap<>();

    private final Set<MessageCallback> givenStatesCallbacks = new HashSet<>();
    private final Map<MessageCallback, Set<Object>> givenStates = new HashMap<>();

    @Override
    public void executeCallbacks(Object state, Object messageType, Object message) {
        super.executeCallbacks(messageType, message);

        callbacksMap
                .getOrDefault(state, new HashMap<>())
                .getOrDefault(messageType, new LinkedList<>())
                .forEach(callback -> invokeCallback(callback, message));

        exceptStatesCallbacks.forEach(callback -> {
            boolean stateIsExcepted = exceptStates
                    .getOrDefault(callback, new HashSet<>())
                    .contains(state);

            if (stateIsExcepted) {
                return;
            }

            invokeCallback(callback, message);
        });

        givenStatesCallbacks.forEach(callback -> {
            boolean stateIsGiven = givenStates
                    .getOrDefault(callback, new HashSet<>())
                    .contains(state);

            if (!stateIsGiven) {
                return;
            }

            invokeCallback(callback, message);
        });
    }

    @Override
    public <T> void addCallback(Class<T> messageClass, Object state, Object messageType, MessageCallback<T> callback) {
        callbacksMap
                .computeIfAbsent(state, key -> new HashMap<>())
                .computeIfAbsent(messageType, key -> new LinkedList<>())
                .add(callback);
    }

    @Override
    public <T> void addCallback(Class<T> messageClass, Collection<Object> states, Object MessageType, MessageCallback<T> callback) {
        givenStatesCallbacks.add(callback);
        givenStates
                .computeIfAbsent(callback, key -> new HashSet<>())
                .addAll(states);
    }

    @Override
    public <T> void addCallbackExcept(Class<T> messageClass, Collection<Object> exceptStates, Object MessageType, MessageCallback<T> callback) {
        exceptStatesCallbacks.add(callback);
        this.exceptStates
                .computeIfAbsent(callback, key -> new HashSet<>())
                .addAll(exceptStates);
    }

    @Override
    public boolean contains(MessageCallback callback) {
        if (super.contains(callback)) {
            return true;
        }

        for (Map<Object, List<MessageCallback>> objectListMap : callbacksMap.values()) {
            for (List<MessageCallback> messageCallbacks : objectListMap.values()) {
                if (messageCallbacks.contains(callback)) {
                    return true;
                }
            }
        }

        if (givenStatesCallbacks.contains(callback)) {
            return true;
        }
        
        if (exceptStatesCallbacks.contains(callback)) {
            return true;
        }

        return false;
    }

    @Override
    public void removeCallback(MessageCallback callback) {
        super.removeCallback(callback);

        callbacksMap
                .values()
                .forEach(map ->
                        map.values().forEach(list -> {
                            list.remove(callback);
                }));

        exceptStatesCallbacks.remove(callback);
        exceptStates.remove(callback);

        givenStatesCallbacks.remove(callback);
        givenStates.remove(callback);
    }
}
