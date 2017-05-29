package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.DefaultMessage;
import edu.agh.io.industryOptimizer.messaging.LinkConfigAgentType;
import edu.agh.io.industryOptimizer.messaging.MessageType;

import java.util.Collection;

public class LinkConfigMessage extends DefaultMessage {
    private final Collection<LinkConfigEntry> configuration;

    public LinkConfigMessage(MessageType messageType, Collection<LinkConfigEntry> configuration) {
        super(messageType);
        this.configuration = configuration;
    }

    public Collection<LinkConfigEntry> getConfiguration() {
        return configuration;
    }

    public enum OperationType {
        LINK, UNLINK
    }

    public class LinkConfigEntry {
        private final OperationType operationType;
        private final LinkConfigAgentType agentType;
        private final AgentIdentifier agentIdentifier;

        public LinkConfigEntry(OperationType operationType, LinkConfigAgentType agentType, AgentIdentifier agentIdentifier) {
            this.operationType = operationType;
            this.agentType = agentType;
            this.agentIdentifier = agentIdentifier;
        }

        public OperationType getOperationType() {
            return operationType;
        }

        public LinkConfigAgentType getAgentType() {
            return agentType;
        }

        public AgentIdentifier getAgentIdentifier() {
            return agentIdentifier;
        }
    }
}
