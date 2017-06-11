package edu.agh.io.industryOptimizer.messaging.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.agh.io.industryOptimizer.agents.AgentType;

public class LinkConfigEntry {
    @JsonProperty("operationType") private final OperationType operationType;
    @JsonProperty("agentType") private final AgentType agentType;
    @JsonProperty("agent") private final String agent;

    @JsonCreator
    public LinkConfigEntry(
            @JsonProperty("operationType") OperationType operationType,
            @JsonProperty("agentType") AgentType agentType,
            @JsonProperty("agent") String agent) {
        this.operationType = operationType;
        this.agentType = agentType;
        this.agent = agent;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public AgentType getAgentType() {
        return agentType;
    }

    public String getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        return "LinkConfigEntry{" +
                "operationType=" + operationType +
                ", agentType=" + agentType +
                ", agent=" + agent +
                '}';
    }
}
