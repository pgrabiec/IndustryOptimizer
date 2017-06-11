package edu.agh.io.industryOptimizer.messaging.messages.util;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;

public class AgentIdApplier extends ConfigApplierImpl<LinkConfigMessage.LinkConfigEntry, AgentIdentifier> {
    public AgentIdApplier() {
        super();
        super.type(LinkConfigMessage.LinkConfigEntry::getAgentType);
        super.argument(LinkConfigMessage.LinkConfigEntry::getAgentIdentifier);
    }
}
