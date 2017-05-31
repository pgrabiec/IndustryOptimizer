package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.ControlMessage;
import edu.agh.io.industryOptimizer.messaging.messages.DataMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import edu.agh.io.industryOptimizer.model.data.DataImpl;
import edu.agh.io.industryOptimizer.model.data.DataValueImpl;
import edu.agh.io.industryOptimizer.model.units.UnitImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InterfaceAgent extends AbstractStatefulAgent {
    private AgentIdentifier productionProcessId;

    private final List<LinkConfigMessage> linkConfigMessagesPending = new LinkedList<>();

    @Override
    protected void setupImpl(StatefulCallbacksUtility utility) {
        // WAITING

        utility.addCallback(
                LinkConfigMessage.class,
                ProductionProcessState.WAITING,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallbackExcept(
                LinkConfigMessage.class,
                Arrays.asList(new ProductionProcessState[] {
                        ProductionProcessState.WAITING
                }),
                MessageType.LINK_CONFIG,
                linkConfigMessagesPending::add
        );


        utility.addCallback(
                ControlMessage.class,
                ProductionProcessState.WAITING,
                MessageType.PROCESS_INIT,
                message -> {
                    setState(ProductionProcessState.INITIALIZING);
                    System.out.println("Initializing - ready in 1,5 s");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        sendMessageToProcess(new ControlMessage(
                                MessageType.PROCESS_READY,
                                new AgentIdentifier(getAID().getName())
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // INITIALIZING

        utility.addCallback(
                ControlMessage.class,
                ProductionProcessState.INITIALIZING,
                MessageType.PROCESS_START,
                message -> {
                    for (int i=0; i<10; i++) {
                        try {
                            sendMessageToProcess(new DataMessage(
                                    MessageType.PROCESS_DATA,
                                    getMyId(),
                                    new DataImpl(
                                            "res" + (System.currentTimeMillis() % 100),
                                            new DataValueImpl(String.valueOf(System.currentTimeMillis() % 30)),
                                            UnitImpl.NUMBER
                                    )
                            ));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        sendMessageToProcess(new ControlMessage(
                                MessageType.PROCESS_FINISHED,
                                new AgentIdentifier(getAID().getName())
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // EXECUTING

        utility.addCallback(
                ControlMessage.class,
                ProductionProcessState.EXECUTING,
                MessageType.PROCESS_STOP,
                message -> {
                    setState(ProductionProcessState.FINALIZING);

                    try {
                        sendMessageToProcess(new DataMessage(
                                MessageType.PROCESS_DATA,
                                getMyId(),
                                new DataImpl(
                                        "final" + (System.currentTimeMillis() % 100),
                                        new DataValueImpl(String.valueOf(System.currentTimeMillis() % 30)),
                                        UnitImpl.NUMBER
                                )
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        sendMessageToProcess(new ControlMessage(
                                MessageType.PROCESS_FINALIZE,
                                new AgentIdentifier(getAID().getName())
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // FINALIZING

        utility.addCallback(
                ControlMessage.class,
                ProductionProcessState.FINALIZING,
                MessageType.PROCESS_FINALIZE,
                message -> {
                    setState(ProductionProcessState.WAITING);
                    linkConfigMessagesPending.forEach(this::applyLinkConfig);
                    linkConfigMessagesPending.clear();
                    System.out.println("Waiting");
                }
        );
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

    private void sendMessageToProcess(Message message) throws IOException {
        if (productionProcessId == null) {
            return;
        }

        sendMessage(productionProcessId, message);
    }
}
