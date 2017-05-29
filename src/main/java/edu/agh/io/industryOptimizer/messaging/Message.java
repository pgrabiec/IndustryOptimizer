package edu.agh.io.industryOptimizer.messaging;

import java.io.Serializable;

public interface Message extends Serializable {
    public MessageType getMessageType();
    public Object getContent();
}
