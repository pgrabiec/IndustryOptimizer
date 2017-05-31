package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.MessageHandler;

import java.util.Collection;

public class LinkConfigMessage extends AbstractMessage {
    private final Collection<LinkConfigEntry> configuration;

    public LinkConfigMessage(MessageType messageType, Collection<LinkConfigEntry> configuration, AgentIdentifier sender) {
        super(messageType, sender);
        this.configuration = configuration;
    }

    public Collection<LinkConfigEntry> getConfiguration() {
        return configuration;
    }

    @Override
    public void accept(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

    public enum OperationType {
        LINK, UNLINK
    }

    public class LinkConfigEntry {
        private final OperationType operationType;
        private final AgentType agentType;
        private final AgentIdentifier agentIdentifier;

        public LinkConfigEntry(OperationType operationType, AgentType agentType, AgentIdentifier agentIdentifier) {
            this.operationType = operationType;
            this.agentType = agentType;
            this.agentIdentifier = agentIdentifier;
        }

        public OperationType getOperationType() {
            return operationType;
        }

        public AgentType getAgentType() {
            return agentType;
        }

        public AgentIdentifier getAgentIdentifier() {
            return agentIdentifier;
        }
    }
}
