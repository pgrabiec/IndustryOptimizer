package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.MessageHandler;
import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;

/**
 * Created by Tomasz on 29.05.2017.
 */
public class BatchIdMessage extends AbstractMessage {
    private final BatchIdentifier id;

    public BatchIdMessage(MessageType messageType, BatchIdentifier id, AgentIdentifier sender) {
        super(messageType, sender);
        this.id = id;
    }

    public BatchIdentifier getBatchId() {
        return id;
    }

    @Override
    public void accept(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
