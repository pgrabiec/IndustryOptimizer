package edu.agh.io.industryOptimizer.messaging;

@FunctionalInterface
public interface MessageCallback<T> {
    public void messageReceived(T message);
}
