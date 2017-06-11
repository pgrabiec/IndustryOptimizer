package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedCallbacksUtilityImpl extends CallbacksUtilityImpl {
    private final Lock lock = new ReentrantLock()

    @Override
    public <T> void addCallback(Class<T> messageClass, Object messageType, MessageCallback<T> callback) {
        super.addCallback(messageClass, messageType, callback);
    }

    @Override
    public void removeCallback(MessageCallback callback) {
        super.removeCallback(callback);
    }

    @Override
    public void executeCallbacks(Object messageType, Object message) {
        super.executeCallbacks(messageType, message);
    }

    @Override
    public boolean contains(MessageCallback callback) {
        return super.contains(callback);
    }

    @Override
    void invokeCallback(MessageCallback callback, Object message) {
        super.invokeCallback(callback, message);
    }
}
