package edu.agh.io.industryOptimizer.messaging;

import edu.agh.io.industryOptimizer.messaging.messages.BatchIdMessage;
import edu.agh.io.industryOptimizer.messaging.messages.ControlMessage;
import edu.agh.io.industryOptimizer.messaging.messages.DataMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;

public interface MessageVisitor {
    public void visitMessage(Message message);
    public void visitLinkConfigMessage(LinkConfigMessage linkConfigMessage);
    public void visitControlMessage(ControlMessage controlMessage);
    public void visitDataMessage(DataMessage dataMessage);
    public void visitBatchIdMessage(BatchIdMessage dataMessage);
}
