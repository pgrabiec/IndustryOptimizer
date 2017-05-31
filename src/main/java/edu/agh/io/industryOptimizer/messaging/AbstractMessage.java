package edu.agh.io.industryOptimizer.messaging;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;

public abstract class AbstractMessage implements Message {
    private final MessageType messageType;
    private final AgentIdentifier sender;

    public AbstractMessage(MessageType messageType, AgentIdentifier sender) {
        this.messageType = messageType;
        this.sender = sender;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public AgentIdentifier getSender() {
        return sender;
    }
}
