package edu.agh.io.industryOptimizer.messaging;

import edu.agh.io.industryOptimizer.messaging.messages.MessageType;

public interface Message {
    public MessageType getMessageType();
    public String getSender();
}