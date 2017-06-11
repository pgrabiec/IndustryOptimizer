package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.agents.impl.processes.ProductionProcessAgentImpl;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.util.ConfigApplierImpl;
import edu.agh.io.industryOptimizer.messaging.messages.util.ConfigEntryApplier;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import edu.agh.io.industryOptimizer.model.DefaultIdentifier;
import edu.agh.io.industryOptimizer.model.Identifier;
import edu.agh.io.industryOptimizer.model.process.ProductionProcess;
import edu.agh.io.industryOptimizer.model.process.ProductionProcessIdentifier;
import edu.agh.io.industryOptimizer.model.process.ProductionProcessImpl;
import jade.core.behaviours.OneShotBehaviour;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public abstract class ProductionProcessAgent extends AbstractStatefulAgent {
    private static final Logger log = Logger.getLogger(ProductionProcessAgent.class.getName());

    private String type;
    private String name;

    private final Set<AgentIdentifier> allInterfaces = new HashSet<>();
    private final Set<AgentIdentifier> confirmedSensors = new HashSet<>();

    private final ArrayList<LinkConfigMessage> configLinksPending = new ArrayList<>();

    private final Set<AgentIdentifier> inputBatchesAgents = new HashSet<>();
    private final Set<AgentIdentifier> outputBatchesAgents = new HashSet<>();

    private AgentIdentifier persistenceAgent = null;

    /**
     * Current production process
     */
    private ProductionProcess productionProcess;

    public ProductionProcessAgent() {
        name = getLocalName();
        type = "unknown";
    }

    @Override
    protected final void setupCallbacksStateful(StatefulCallbacksUtility utility) {
        Object[] arguments = getArguments();

        if (arguments.length == 2) {
            try {
                type = (String) arguments[0];
            } catch (ClassCastException e) {
                type = "unknown";
            }
            try {
                name = (String) arguments[1];
            } catch (ClassCastException e) {
                name = getLocalName();
            }
        }

        // WAITING
        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.WAITING,
                DocumentMessage.MessageType.PROCESS_INIT,
                message -> {
                    setProcessState(ProductionProcessState.INITIALIZING);
                    productionProcess = new ProductionProcessImpl(
                            new ProductionProcessIdentifier(ObjectId.get()),
                            name,
                            type
                    );
                    sendToAllSensors(new DocumentMessage(
                            DocumentMessage.MessageType.PROCESS_INIT,
                            getMyId(),
                            new Document()
                    ));
                });

        utility.addCallback(
                LinkConfigMessage.class,
                ProductionProcessState.WAITING,
                LinkConfigMessage.MessageType.LINK_CONFIG,
                this::applyLinkConfig);

        utility.addCallbackExcept(
                LinkConfigMessage.class,
                Arrays.asList(new ProductionProcessState[]{
                        ProductionProcessState.WAITING
                }),
                LinkConfigMessage.MessageType.LINK_CONFIG,
                configLinksPending::add);

        // INITIALIZING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.INITIALIZING,
                DocumentMessage.MessageType.PROCESS_READY,
                message -> addInterfaceConfirmation(
                        message.getSender(),
                        () -> {
                            clearConfirmation();
                            setProcessState(ProductionProcessState.EXECUTING);
                            sendToAllSensors(
                                    new DocumentMessage(
                                            DocumentMessage.MessageType.PROCESS_START,
                                            getMyId(),
                                            new Document())
                            );
                        }));


        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.INITIALIZING,
                DocumentMessage.MessageType.PROCESS_START,
                message -> {
                    clearConfirmation();
                    setProcessState(ProductionProcessState.EXECUTING);
                    sendToAllSensors(
                            new DocumentMessage(
                                    DocumentMessage.MessageType.PROCESS_START,
                                    getMyId(),
                                    new Document())
                    );
                });

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.INITIALIZING,
                DocumentMessage.MessageType.BATCH_LAST,
                message -> {
                    if (persistenceAgent == null) {
//                        System.out.println("Received batch id but no persistence present");
                        return;
                    }

                    try {
                        sendMessage(persistenceAgent, message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.INITIALIZING,
                DocumentMessage.MessageType.BATCH_NEW,
                message -> {
                    if (persistenceAgent == null) {
//                        System.out.println("Received batch id but no persistence present");
                        return;
                    }

                    try {
                        sendMessage(persistenceAgent, message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // EXECUTING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.EXECUTING,
                DocumentMessage.MessageType.PROCESS_FINISHED,
                message -> addInterfaceConfirmation(
                        message.getSender(),
                        () -> {
                            clearConfirmation();
                            setProcessState(ProductionProcessState.FINALIZING);
                            sendToAllSensors(
                                    new DocumentMessage(
                                            DocumentMessage.MessageType.PROCESS_STOP,
                                            getMyId(),
                                            new Document())
                            );
                        }
                ));

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.EXECUTING,
                DocumentMessage.MessageType.PROCESS_STOP,
                message -> {
                    clearConfirmation();
                    setProcessState(ProductionProcessState.FINALIZING);
                    sendToAllSensors(
                            new DocumentMessage(
                                    DocumentMessage.MessageType.PROCESS_STOP,
                                    getMyId(),
                                    new Document())
                    );
                });

        // FINALIZING

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.FINALIZING,
                DocumentMessage.MessageType.PROCESS_FINALIZE,
                message -> addInterfaceConfirmation(
                        message.getSender(),
                        () -> {
                            log.debug("Finalizing gracefully");
                            clearConfirmation();
                            setProcessState(ProductionProcessState.EXPANDING);
                            sendToAllSensors(
                                    new DocumentMessage(
                                            DocumentMessage.MessageType.PROCESS_FINALIZE,
                                            getMyId(),
                                            new Document())
                            );
                        }
                ));

        utility.addCallback(
                DocumentMessage.class,
                ProductionProcessState.FINALIZING,
                DocumentMessage.MessageType.PROCESS_FORCE_FINALIZE,
                message -> {
                    clearConfirmation();
                    setProcessState(ProductionProcessState.EXPANDING);
                    sendToAllSensors(
                            new DocumentMessage(
                                    DocumentMessage.MessageType.PROCESS_FINALIZE,
                                    getMyId(),
                                    new Document())
                    );
                    sendProcessData();
                    productionProcess = new ProductionProcessImpl(
                            new ProductionProcessIdentifier(
                                    ObjectId.get()),
                            name,
                            type
                    );
                    setProcessState(ProductionProcessState.WAITING);
                });

        // EXPANDING

        // STATELESS

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[]{ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_PARAM_CONTROL,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }
                    productionProcess.controlParameters()
                            .add(message.getDocument());
                });

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[]{ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_PARAM_OUT,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }
                    productionProcess.outputParameters()
                            .add(message.getDocument());
                });

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[]{ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_RES_IN,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }

                    Identifier identifier = DefaultIdentifier.fromDocument(message.getDocument());

                    if (identifier == null) {
                        return;
                    }

                    productionProcess.inputBatches()
                            .add(identifier);
                });

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[]{ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_RES_IN_OTHER,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }
                    productionProcess.otherInput()
                            .add(message.getDocument());
                });

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[]{ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_RES_OUT,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }

                    Identifier identifier = DefaultIdentifier.fromDocument(message.getDocument());

                    if (identifier == null) {
                        return;
                    }

                    productionProcess.outputBatches()
                            .add(identifier);
                });

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[]{ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_RES_OUT_OTHER,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }

                    productionProcess.otherOutput()
                            .add(message.getDocument());
                });

    }

    private void sendToAllSensors(Message message) {
        addBehaviour((new OneShotBehaviour() {
            @Override
            public void action() {
                for (AgentIdentifier sensor : allInterfaces) {
                    try {
                        sendMessage(sensor, message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

    }

    private void sendProcessData() {
        if (persistenceAgent == null) {
            return;
        }

        try {
            sendMessage(
                    persistenceAgent,
                    new DocumentMessage(
                            DocumentMessage.MessageType.PROCESS_DATA,
                            getMyId(),
                            productionProcess.toDocument()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyLinkConfig(LinkConfigMessage config) {
//        System.out.println("Applying link config " + config);
        config.getConfiguration().forEach(linkConfigEntry -> {

            new ConfigEntryApplier()

                    .callback(AgentType.BATCH_INPUT, entry -> {

                        if (entry.getOperationType() == LinkConfigMessage.OperationType.LINK) {
                            this.inputBatchesAgents.add(entry.getAgentIdentifier());
                            onBatchInputLinked(entry.getAgentIdentifier());

                        } else if (entry.getOperationType() == LinkConfigMessage.OperationType.UNLINK) {
                            this.inputBatchesAgents.remove(entry.getAgentIdentifier());
                            onBatchInputUnlinked(entry.getAgentIdentifier());
                        }
                    })

                    .callback(AgentType.BATCH_OUTPUT, entry -> {
                        if (entry.getOperationType() == LinkConfigMessage.OperationType.LINK) {
                            this.outputBatchesAgents.add(entry.getAgentIdentifier());
                            onBatchOutputLinked(entry.getAgentIdentifier());

                        } else if (entry.getOperationType() == LinkConfigMessage.OperationType.UNLINK) {
                            this.outputBatchesAgents.remove(entry.getAgentIdentifier());
                            onBatchOutputUnlinked(entry.getAgentIdentifier());
                        }
                    })

                    .callback(AgentType.INTERFACE, entry -> {
                        if (entry.getOperationType() == LinkConfigMessage.OperationType.LINK) {
                            this.allInterfaces.add(entry.getAgentIdentifier());
                            onInterfaceLinked(entry.getAgentIdentifier());

                        } else if (entry.getOperationType() == LinkConfigMessage.OperationType.UNLINK) {
                            this.allInterfaces.remove(entry.getAgentIdentifier());
                            onInterfaceUnlinked(entry.getAgentIdentifier());
                        }
                    })

                    .callback(AgentType.PERSISTENCE, entry -> {
                        if (entry.getOperationType() == LinkConfigMessage.OperationType.LINK) {
                            this.persistenceAgent = entry.getAgentIdentifier();
                            onPersistenceLinked(entry.getAgentIdentifier());

                        } else if (entry.getOperationType() == LinkConfigMessage.OperationType.UNLINK) {
                            this.persistenceAgent = null;
                            onPersistenceUnlinked();
                        }
                    })

                    .execute(linkConfigEntry);
        });
    }

    private void addInterfaceConfirmation(AgentIdentifier interfaceId, Runnable allConfirmedCallback) {
        log.debug("FINALIZE CONFIRMATION: " + interfaceId);

        if (!allInterfaces.contains(interfaceId)) {
            return; // That sensor is not linked with this process
        }

        if (!confirmedSensors.add(interfaceId)) {
            return; // That sensor has already been declared ready
        }

        if (allInterfaces.size() != confirmedSensors.size()) {
            return; // There are some sensors that are not confirmed yet
        }

        allConfirmedCallback.run();
    }

    private void clearConfirmation() {
        confirmedSensors.clear();
    }

    protected void onPersistenceUnlinked() {
    }

    protected void onPersistenceLinked(AgentIdentifier agentIdentifier) {
    }

    protected void onInterfaceUnlinked(AgentIdentifier agentIdentifier) {
    }

    protected void onInterfaceLinked(AgentIdentifier agentIdentifier) {
    }

    protected void onBatchOutputUnlinked(AgentIdentifier batch) {
    }

    protected void onBatchOutputLinked(AgentIdentifier batch) {
    }

    protected void onBatchInputUnlinked(AgentIdentifier batch) {
    }

    protected void onBatchInputLinked(AgentIdentifier batch) {
    }
}
