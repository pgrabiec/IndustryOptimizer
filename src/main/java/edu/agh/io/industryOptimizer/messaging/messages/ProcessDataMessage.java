package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;

/**
 * Created by Tomasz on 03.06.2017.
 */
public class ProcessDataMessage extends AbstractMessage {
    public ProcessDataMessage(MessageType messageType, AgentIdentifier sender) {
        super(messageType, sender);
    }
}
