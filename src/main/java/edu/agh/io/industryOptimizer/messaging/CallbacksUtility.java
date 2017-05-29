package edu.agh.io.industryOptimizer.messaging;

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
public interface CallbacksUtility {
    public void executeCallbacks(Object state, Object messageType, Object message);

    public void addCallback(Object state, Object messageType, MessageCallback callback);

    public void addCallbackAllStates(Object MessageType, MessageCallback callback);

    public void removeCallback(MessageCallback callback);
}
