package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.messaging.DefaultMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.MessageVisitor;

/**
 * Created by Tomasz on 29.05.2017.
 */
public class BatchIdMessage extends DefaultMessage {
    private String id;

    public BatchIdMessage(MessageType messageType, String id) {
        super(messageType);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void accept(MessageVisitor visitor) {

    }
}
