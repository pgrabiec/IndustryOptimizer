package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class InterfaceAgent extends AbstractStatefulAgent {
    private AgentIdentifier productionProcessId;

    private final List<LinkConfigMessage> linkConfigMessagesPending = new LinkedList<>();

    @Override
    protected final void setupImpl(StatefulCallbacksUtility utility) {
        // WAITING

        utility.addCallback(
                LinkConfigMessage.class,
                ProductionProcessState.WAITING,
                LinkConfigMessage.MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallbackExcept(
                LinkConfigMessage.class,
                Arrays.asList(new ProductionProcessState[] {
                        ProductionProcessState.WAITING
                }),
                LinkConfigMessage.MessageType.LINK_CONFIG,
                linkConfigMessagesPending::add
        );

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.WAITING,
                DocumentMessage.MessageType.PROCESS_INIT,
                message -> {
                    setState(ProductionProcessState.INITIALIZING);
                    initialize();
                }
        );

        // INITIALIZING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.INITIALIZING,
                DocumentMessage.MessageType.PROCESS_START,
                message -> {
                    setState(ProductionProcessState.EXECUTING);
                    execute();
                }
        );

        // EXECUTING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.EXECUTING,
                DocumentMessage.MessageType.PROCESS_STOP,
                message -> {
                    setState(ProductionProcessState.FINALIZING);
                    finalizing();
                }
        );

        // FINALIZING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.FINALIZING,
                DocumentMessage.MessageType.PROCESS_FINALIZE,
                message -> {
                    setState(ProductionProcessState.WAITING);
                    linkConfigMessagesPending.forEach(this::applyLinkConfig);
                    linkConfigMessagesPending.clear();
                    waiting();
                }
        );

        preSetup();

        setState(ProductionProcessState.WAITING);
        waiting();
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            if (!linkConfigEntry.getAgentType()
                    .equals(AgentType.PROCESS)) {
                return;
            }

            switch (linkConfigEntry.getOperationType()) {
                case LINK:
                    this.productionProcessId = linkConfigEntry.getAgentIdentifier();
                    break;
                case UNLINK:
                    this.productionProcessId = null;
                    break;
            }
        });
    }

    protected void sendMessageToProcess(Message message) throws IOException {
        if (productionProcessId == null) {
            return;
        }

        sendMessage(productionProcessId, message);
    }

    protected abstract void preSetup();
    protected abstract void waiting();
    protected abstract void initialize();
    protected abstract void execute();
    protected abstract void finalizing();
}
