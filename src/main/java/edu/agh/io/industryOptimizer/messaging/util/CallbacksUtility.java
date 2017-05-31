package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

public interface CallbacksUtility {
    public <T> void addCallback(Class<T> messageClass, Object messageType, MessageCallback<T> callback);

    public void executeCallbacks(Object messageType, Object message);

    public void removeCallback(MessageCallback callback);

    public boolean contains(MessageCallback callback);
}
