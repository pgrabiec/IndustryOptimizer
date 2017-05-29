package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.messaging.DefaultMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.MessageVisitor;

public class ControlMessage extends DefaultMessage {
    public ControlMessage(MessageType messageType) {
        super(messageType);
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visitControlMessage(this);
    }
}
