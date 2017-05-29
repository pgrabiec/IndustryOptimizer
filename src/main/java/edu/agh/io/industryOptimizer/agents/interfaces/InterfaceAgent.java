package edu.agh.io.industryOptimizer.agents.interfaces;

import edu.agh.io.industryOptimizer.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.CallbacksUtility;
<<<<<<< HEAD
import edu.agh.io.industryOptimizer.messaging.CallbacksUtilityImpl;
=======
import edu.agh.io.industryOptimizer.messaging.Message;
>>>>>>> bdcf2ac58e93787c9c58d44137b664fd10ca894f
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

<<<<<<< HEAD
//    private void handleMessage(Message message) {
//        utility.getCallback(state, message.getMessageType())
//                .forEach(callback ->
//                        callback.messageReceived(message.getContent())
//                );
//    }
=======
    private void initialize() {

    }
>>>>>>> bdcf2ac58e93787c9c58d44137b664fd10ca894f

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
