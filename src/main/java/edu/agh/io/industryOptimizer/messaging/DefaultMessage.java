package edu.agh.io.industryOptimizer.messaging;

public abstract class DefaultMessage implements Message {
    private final MessageType messageType;

    public DefaultMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }
}
