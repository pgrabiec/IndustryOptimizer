package edu.agh.io.industryOptimizer.messaging.messages.util;

import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigEntry;
import edu.agh.io.industryOptimizer.messaging.messages.OperationType;

public class AgentIdApplier extends ConfigApplierImpl<LinkConfigEntry, String> {
    public AgentIdApplier() {
        super();
        super.type(LinkConfigEntry::getAgentType);
        super.argument(entry -> {
            if (entry.getOperationType() == OperationType.LINK) {
                return entry.getAgent();
            }
            return null;
        });
    }
}
