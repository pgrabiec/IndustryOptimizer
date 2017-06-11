package edu.agh.io.industryOptimizer.messaging.messages.util;

import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigEntry;

public class ConfigEntryApplier
        extends ConfigApplierImpl<LinkConfigEntry, LinkConfigEntry> {
    public ConfigEntryApplier() {
        super();
        super.type(LinkConfigEntry::getAgentType);
        super.argument(entry -> entry);
    }
}
