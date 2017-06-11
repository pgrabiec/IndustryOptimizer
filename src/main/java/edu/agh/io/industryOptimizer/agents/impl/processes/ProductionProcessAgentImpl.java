package edu.agh.io.industryOptimizer.agents.impl.processes;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.impl.ProductionProcessAgent;
import org.apache.log4j.Logger;

public class ProductionProcessAgentImpl extends ProductionProcessAgent {
    private static final Logger log = Logger.getLogger(ProductionProcessAgentImpl.class.getName());

    @Override
    protected void onPersistenceUnlinked() {
        log.debug("onPersistenceUnlinked");
    }

    @Override
    protected void onPersistenceLinked(AgentIdentifier agentIdentifier) {
        log.debug("onPersistenceLinked");
    }

    @Override
    protected void onInterfaceUnlinked(AgentIdentifier agentIdentifier) {
        log.debug("onInterfaceUnlinked");
    }

    @Override
    protected void onInterfaceLinked(AgentIdentifier agentIdentifier) {
        log.debug("onInterfaceLinked");
    }

    @Override
    protected void onBatchOutputUnlinked(AgentIdentifier batch) {
        log.debug("onBatchOutputUnlinked");
    }

    @Override
    protected void onBatchOutputLinked(AgentIdentifier batch) {
        log.debug("onBatchOutputLinked");
    }

    @Override
    protected void onBatchInputUnlinked(AgentIdentifier batch) {
        log.debug("onBatchInputUnlinked");
    }

    @Override
    protected void onBatchInputLinked(AgentIdentifier batch) {
        log.debug("onBatchInputLinked");
    }
}
