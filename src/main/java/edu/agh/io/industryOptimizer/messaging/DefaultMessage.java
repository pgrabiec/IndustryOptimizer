package edu.agh.io.industryOptimizer.messaging;

public class DefaultMessage implements Message {
    private final MessageType messageType;
    private final Object content;

    public DefaultMessage(MessageType messageType, Object content) {
        this.messageType = messageType;
        this.content = content;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public Object getContent() {
        return content;
    }
}
