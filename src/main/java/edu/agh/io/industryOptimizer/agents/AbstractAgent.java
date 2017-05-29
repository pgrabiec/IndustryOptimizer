package edu.agh.io.industryOptimizer.agents;

public abstract class AbstractAgent extends AbstractStatefulAgent {
    private final Object object = new Object();

    protected ProductionProcessState getProcessState() {
        return ProductionProcessState.values()[0];
    }
}
