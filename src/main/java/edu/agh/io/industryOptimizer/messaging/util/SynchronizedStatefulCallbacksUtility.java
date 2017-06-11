package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.MessageCallback;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedStatefulCallbacksUtility extends StatefulCallbacksUtilityImpl {
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
    public void executeCallbacks(Object state, Object messageType, Object message) {
        lock.lock();
        try {
            super.executeCallbacks(state, messageType, message);
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
    void invokeCallback(MessageCallback callback, Object message) {
        lock.lock();
        try {
            super.invokeCallback(callback, message);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> void addCallback(Class<T> messageClass, Object state, Object messageType, MessageCallback<T> callback) {
        lock.lock();
        try {
            super.addCallback(messageClass, state, messageType, callback);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> void addCallback(Class<T> messageClass, Collection<Object> states, Object MessageType, MessageCallback<T> callback) {
        lock.lock();
        try {
            super.addCallback(messageClass, states, MessageType, callback);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> void addCallbackExcept(Class<T> messageClass, Collection<Object> exceptStates, Object MessageType, MessageCallback<T> callback) {
        lock.lock();
        try {
            super.addCallbackExcept(messageClass, exceptStates, MessageType, callback);
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
    public void removeCallback(MessageCallback callback) {
        lock.lock();
        try {
            super.removeCallback(callback);
        } finally {
            lock.unlock();
        }
    }
}
