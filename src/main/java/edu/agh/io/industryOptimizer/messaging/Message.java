package edu.agh.io.industryOptimizer.messaging;


import edu.agh.io.industryOptimizer.AgentIdentifier;

import java.io.Serializable;

public class Message implements Serializable {
    private final AgentIdentifier source;

    public Message(AgentIdentifier source) {
        this.source = source;
    }

    public AgentIdentifier getSource() {
        return source;
    }
}