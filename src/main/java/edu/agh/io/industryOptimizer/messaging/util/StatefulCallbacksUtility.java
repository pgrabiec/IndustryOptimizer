package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

import java.util.Collection;

public interface StatefulCallbacksUtility extends CallbacksUtility {
    public void executeCallbacks(Object state, Object messageType, Object message);

    public <T> void addCallback(
            Class<T> messageClass,
            Object state,
            Object messageType,
            MessageCallback<T> callback);

    public <T> void addCallback(
            Class<T> messageClass,
            Collection<Object> states,
            Object MessageType,
            MessageCallback<T> callback);

    public <T> void addCallbackExcept(
            Class<T> messageClass,
            Collection<Object> exceptStates,
            Object MessageType,
            MessageCallback<T> callback);
}
