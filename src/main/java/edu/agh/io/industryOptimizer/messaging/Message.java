package edu.agh.io.industryOptimizer.messaging;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;

import java.io.Serializable;

public interface Message extends Serializable {
    public Object getMessageType();
    public AgentIdentifier getSender();
}
