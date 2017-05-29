package edu.agh.io.industryOptimizer.messaging;

import java.util.*;

/**
 * Example use:
 *
 * utility.addCallback(1, 1, message -> {
 * System.out.println("Received message on state 1 message 1: " + message);
 * });
 *
 * utility.addCallback(1, 2, message -> {
 * System.out.println("1: Received message on state 1 message 2: " + message);
 * });
 *
 * utility.addCallback(1, 2, message -> {
 * System.out.println("2: Received message on state 1 message 2: " + message);
 * });
 *
 * utility.getCallback(1, 2).forEach(
 * messageCallback -> messageCallback.messageReceived("MSG")
 );
 * */
public class CallbacksUtility {
    private final Map<Object, Map<Object, List<MessageCallback>>> callbacksMap = new HashMap<>();

    public Collection<MessageCallback> getCallback(Object state, Object messageType) {
        return callbacksMap
                .computeIfAbsent(
                        state,
                        key -> new HashMap<>())
                .getOrDefault(
                        messageType,
                        new LinkedList<>()
                );
    }

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
}
