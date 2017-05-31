package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CallbacksUtilityImpl implements CallbacksUtility {
    private final Map<Object, List<MessageCallback>> callbacksMap = new HashMap<>();

    @Override
    public <T> void addCallback(Class<T> messageClass, Object messageType, MessageCallback<T> callback) {
        callbacksMap
                .computeIfAbsent(
                        messageType,
                        key -> new LinkedList<>())
                .add(callback);
    }

    @Override
    public void removeCallback(MessageCallback callback) {
        callbacksMap.values().forEach(list ->
                list.remove(callback)
        );
    }

    @Override
    public void executeCallbacks(Object messageType, Object message) {
        callbacksMap.getOrDefault(messageType, new LinkedList<>())
                .forEach((MessageCallback callback) -> {
                    invokeCallback(callback, message);
                });
    }

    @Override
    public boolean contains(MessageCallback callback) {
        for (List<MessageCallback> messageCallbacks : callbacksMap.values()) {
            if (messageCallbacks.contains(callback)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    void invokeCallback(MessageCallback callback, Object message) {
        try {
            callback.messageReceived(message);
        } catch (ClassCastException e) {
            // Ignore the messages of wrong types
        }
    }
}
