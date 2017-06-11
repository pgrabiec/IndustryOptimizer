package edu.agh.io.industryOptimizer.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;

public abstract class AbstractMessage implements Message {
    @JsonProperty("messageType")
    private final MessageType messageType;

    @JsonProperty("sender")
    private final String sender;

    @JsonCreator
    public AbstractMessage(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("sender") String sender) {
        this.messageType = messageType;
        this.sender = sender;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String getSender() {
        return sender;
    }
}
