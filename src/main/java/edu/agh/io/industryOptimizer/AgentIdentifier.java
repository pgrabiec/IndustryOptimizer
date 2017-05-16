package edu.agh.io.industryOptimizer;

import java.io.Serializable;

public class AgentIdentifier implements Serializable {
    private final String id;

    public AgentIdentifier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
