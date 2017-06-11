package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedCallbacksUtility extends CallbacksUtilityImpl {
    private final Lock lock = new ReentrantLock();

    @Override
    public <T> void addCallback(Class<T> messageClass, Object messageType, MessageCallback<T> callback) {
        lock.lock();
        try {
            super.addCallback(messageClass, messageType, callback);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeCallback(MessageCallback callback) {
        lock.lock();
        try {

            super.removeCallback(callback);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void executeCallbacks(Object messageType, Object message) {
        lock.lock();
        try {

            super.executeCallbacks(messageType, message);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(MessageCallback callback) {
        lock.lock();
        try {

            return super.contains(callback);
        } finally {
            lock.unlock();
        }
    }

    @Override
    void invokeCallback(MessageCallback callback, Object message) {
        lock.lock();
        try {

            super.invokeCallback(callback, message);
        } finally {
            lock.unlock();
        }
    }
}
