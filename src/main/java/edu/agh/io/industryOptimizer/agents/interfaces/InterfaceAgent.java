package edu.agh.io.industryOptimizer.agents.interfaces;

import edu.agh.io.industryOptimizer.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;

public class InterfaceAgent extends AbstractStatefulAgent {
    private ProductionProcessState state = ProductionProcessState.WAITING;
    private AgentIdentifier productionProcessId;

    @Override
    protected void setupImpl(CallbacksUtility utility) {
        utility.addCallback(
                ProductionProcessState.WAITING,
                MessageType.PROCESS_INIT,
                message -> {
                    state = ProductionProcessState.INITIALIZING;
                    initialize();
                }
        );

        utility.addCallback(
                ProductionProcessState.WAITING,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );
    }



    private void applyLinkConfig(Object message) {}

    private void applyLinkConfig(LinkConfigMessage message) {

    }

    private void initialize() {

    }

    @Override
    protected ProductionProcessState getProcessState() {
        return state;
    }

    @Override
    public void visitMessage(Message message) {

    }

    @Override
    public void visitLinkConfigMessage(LinkConfigMessage message) {

    }
}
