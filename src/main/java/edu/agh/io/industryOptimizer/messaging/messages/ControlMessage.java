package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;

public class ControlMessage extends AbstractMessage {

    public ControlMessage(MessageType messageType, AgentIdentifier sender) {
        super(messageType, sender);
    }
}