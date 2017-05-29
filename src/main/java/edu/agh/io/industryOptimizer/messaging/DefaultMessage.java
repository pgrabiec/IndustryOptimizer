package edu.agh.io.industryOptimizer.messaging;

public class DefaultMessage implements Message {
    private final MessageType messageType;

    public DefaultMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

}
