package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import edu.agh.io.industryOptimizer.messaging.MessageHandler;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.model.data.Data;

/**
 * Created by Tomasz on 29.05.2017.
 */
public class DataMessage extends AbstractMessage {
    private final Data data;

    public DataMessage(MessageType messageType, AgentIdentifier sender, Data data) {
        super(messageType, sender);

        this.data = data;
    }

    public Data getData() {
        return data;
    }

    @Override
    public void accept(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
