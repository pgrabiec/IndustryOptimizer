package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.util.AgentIdApplier;
import edu.agh.io.industryOptimizer.messaging.messages.util.ConfigApplierImpl;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractInterfaceAgent extends AbstractStatefulAgent {
    private final List<LinkConfigMessage> linkConfigMessagesPending = new LinkedList<>();
    private String processAgent;

    public AbstractInterfaceAgent(ProductionProcessState initialState, String productionProcessId) {
        super(initialState);
        this.processAgent = productionProcessId;
    }

    public AbstractInterfaceAgent(String productionProcessId) {
        super(ProductionProcessState.WAITING);
        this.processAgent = productionProcessId;
    }

    public AbstractInterfaceAgent() {
        super(ProductionProcessState.WAITING);
    }

    @Override
    protected final void setupCallbacksStateful(StatefulCallbacksUtility utility) {
        // WAITING

        utility.addCallback(
                LinkConfigMessage.class,
                ProductionProcessState.WAITING,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallbackExcept(
                LinkConfigMessage.class,
                Arrays.asList(new ProductionProcessState[]{
                        ProductionProcessState.WAITING
                }),
                MessageType.LINK_CONFIG,
                linkConfigMessagesPending::add
        );

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.WAITING,
                MessageType.PROCESS_INIT,
                message -> {
                    setProcessState(ProductionProcessState.INITIALIZING);
                    onInitializing();
                }
        );

        // INITIALIZING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.INITIALIZING,
                MessageType.PROCESS_START,
                message -> {
                    setProcessState(ProductionProcessState.EXECUTING);
                    onExecuting();
                }
        );

        // EXECUTING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.EXECUTING,
                MessageType.PROCESS_STOP,
                message -> {
                    setProcessState(ProductionProcessState.FINALIZING);
                    onFinalizing();
                }
        );

        // FINALIZING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.FINALIZING,
                MessageType.PROCESS_FINALIZE,
                message -> {
                    setProcessState(ProductionProcessState.WAITING);
                    linkConfigMessagesPending.forEach(this::applyLinkConfig);
                    linkConfigMessagesPending.clear();
                    onWaiting();
                }
        );
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            new AgentIdApplier()
                    .callback(AgentType.PROCESS, id -> {
                        this.processAgent = id;

                        if (id != null) {
                            onProcessAgentLinked();
                        } else {
                            onProcessAgentUnlinked();
                        }
                    })

                    .execute(linkConfigEntry);
        });
    }

    protected final void sendMessageToProcess(Message message) throws IOException {
        if (processAgent == null) {
            System.out.println("Null process");
            return;
        }

        sendMessage(processAgent, message);
    }

    protected final boolean isProcessAgentLinked() {
        return processAgent != null;
    }

    protected final String getProcessAgent() {
        return processAgent;
    }

    protected abstract void onWaiting();

    protected abstract void onInitializing();

    protected abstract void onExecuting();

    protected abstract void onFinalizing();

    protected void onProcessAgentLinked() {
    }

    protected void onProcessAgentUnlinked() {
    }
    @Override
    protected final AgentType agentType() {
        return AgentType.INTERFACE;
    }
}
