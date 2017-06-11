package edu.agh.io.industryOptimizer.agents.impl.processes;

import edu.agh.io.industryOptimizer.agents.impl.ProductionProcessAgent;
import org.apache.log4j.Logger;

public class ProductionProcessAgentImpl extends ProductionProcessAgent {
    private static final Logger log = Logger.getLogger(ProductionProcessAgentImpl.class.getName());

    @Override
    protected void onPersistenceUnlinked() {
        log.debug("onPersistenceUnlinked");
    }

    @Override
    protected void onPersistenceLinked(String String) {
        log.debug("onPersistenceLinked");
    }

    @Override
    protected void onInterfaceUnlinked(String String) {
        log.debug("onInterfaceUnlinked");
    }

    @Override
    protected void onInterfaceLinked(String String) {
        log.debug("onInterfaceLinked");
    }

    @Override
    protected void onBatchOutputUnlinked(String batch) {
        log.debug("onBatchOutputUnlinked");
    }

    @Override
    protected void onBatchOutputLinked(String batch) {
        log.debug("onBatchOutputLinked");
    }

    @Override
    protected void onBatchInputUnlinked(String batch) {
        log.debug("onBatchInputUnlinked");
    }

    @Override
    protected void onBatchInputLinked(String batch) {
        log.debug("onBatchInputLinked");
    }
}
