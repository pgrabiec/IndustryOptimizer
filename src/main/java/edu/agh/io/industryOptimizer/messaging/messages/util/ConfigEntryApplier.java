package edu.agh.io.industryOptimizer.messaging.messages.util;

import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;

public class ConfigEntryApplier
        extends ConfigApplierImpl<LinkConfigMessage.LinkConfigEntry, LinkConfigMessage.LinkConfigEntry> {
    public ConfigEntryApplier() {
        super();
        super.type(LinkConfigMessage.LinkConfigEntry::getAgentType);
        super.argument(entry -> entry);
    }
}
