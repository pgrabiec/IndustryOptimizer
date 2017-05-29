package edu.agh.io.industryOptimizer.messaging;

import java.util.Collection;

public interface CallbacksUtility {
    public void executeCallbacks(Object state, Object messageType, Object message);

    public void addCallback(Object state, Object messageType, MessageCallback callback);

    public void addCallbackAllStates(Object MessageType, MessageCallback callback);

    public void removeCallback(MessageCallback callback);

    public void addCallbackAllStatesExcept(Object MessageType, MessageCallback callback, Collection<Object> exceptStates);
}
