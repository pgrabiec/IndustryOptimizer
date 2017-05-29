package edu.agh.io.industryOptimizer.messaging;

import edu.agh.io.industryOptimizer.messaging.messages.ControlMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;

public interface MessageVisitor {
    public void visitMessage(Message message);
    public void visitLinkConfigMessage(LinkConfigMessage linkConfigMessage);
    public void visitControlMessage(ControlMessage controlMessage);
}
