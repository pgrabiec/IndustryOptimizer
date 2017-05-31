package edu.agh.io.industryOptimizer.messaging;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;

import java.io.Serializable;

public interface Message extends Serializable {
    public MessageType getMessageType();
    public AgentIdentifier getSender();
}
