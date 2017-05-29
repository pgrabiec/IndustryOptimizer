package edu.agh.io.industryOptimizer.messaging;

@FunctionalInterface
public interface MessageCallback {

    public void messageReceived(Object message);
}
