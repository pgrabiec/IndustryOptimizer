package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.RequestType;

/**
 * Created by Tomasz on 03.06.2017.
 */
public class RequestMessage extends AbstractMessage {
    private RequestType requestType;

    public RequestMessage(MessageType messageType, AgentIdentifier sender, RequestType requestType) {
        super(messageType, sender);
        this.requestType = requestType;
    }
}
