package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import edu.agh.io.industryOptimizer.model.DefaultIdentifier;
import edu.agh.io.industryOptimizer.model.Identifier;
import edu.agh.io.industryOptimizer.model.process.ProductionProcess;
import edu.agh.io.industryOptimizer.model.process.ProductionProcessIdentifier;
import edu.agh.io.industryOptimizer.model.process.ProductionProcessImpl;
import jade.core.behaviours.OneShotBehaviour;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class ProductionProcessAgent extends AbstractStatefulAgent {
    private String type;
    private String name;

    private final Set<AgentIdentifier> allSensors = new HashSet<>();
    private final Set<AgentIdentifier> confirmedSensors = new HashSet<>();

    private final ArrayList<LinkConfigMessage> configLinksPending = new ArrayList<>();

    private final Set<AgentIdentifier> inputBatchesAgents = new HashSet<>();
    private final Set<AgentIdentifier> outputBatchesAgents = new HashSet<>();

    private AgentIdentifier persistenceAgent = null;

    /**
     * Current production process
     * */
    private ProductionProcess productionProcess;

    public ProductionProcessAgent() {
        name = getLocalName();
        type = "unknown";
    }

    private void sendToAllSensors(Message message) {
		addBehaviour((new OneShotBehaviour() {
			@Override
			public void action() {
            for(AgentIdentifier sensor: allSensors) {
                try {
                    sendMessage(sensor, message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
			}
		}));

	}

	@Override
	protected void setupImpl(StatefulCallbacksUtility utility) {
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
                    setState(ProductionProcessState.INITIALIZING);
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
            Arrays.asList(new ProductionProcessState[] {
                    ProductionProcessState.WAITING
            }),
            LinkConfigMessage.MessageType.LINK_CONFIG,
            configLinksPending::add);

        // INITIALIZING

        utility.addCallback(
            DocumentMessage.class,
            ProductionProcessState.INITIALIZING,
            DocumentMessage.MessageType.PROCESS_READY,
            message -> addSensorConfirmation(
                message.getSender(),
                () -> {
                    clearConfirmation();
                    setState(ProductionProcessState.EXECUTING);
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
                setState(ProductionProcessState.EXECUTING);
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
                    System.out.println("Received batch id but no persistence present");
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
                    System.out.println("Received batch id but no persistence present");
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
            message -> addSensorConfirmation(
                message.getSender(),
                () -> {
                    clearConfirmation();
                    setState(ProductionProcessState.FINALIZING);
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
                setState(ProductionProcessState.FINALIZING);
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
                message -> addSensorConfirmation(
                    message.getSender(),
                    () -> {
                        clearConfirmation();
                        setState(ProductionProcessState.EXPANDING);
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
                    setState(ProductionProcessState.EXPANDING);
                    sendToAllSensors(
                        new DocumentMessage(
                                DocumentMessage.MessageType.PROCESS_FINALIZE,
                                getMyId(),
                                new Document())
                    );
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setState(ProductionProcessState.EXPANDING);
                    sendProcessData();
                    setState(ProductionProcessState.WAITING);
                });

        // EXPANDING

        // STATELESS

        utility.addCallbackExcept(
                DocumentMessage.class,
                Arrays.asList(new Object[] {ProductionProcessState.WAITING}),
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
                Arrays.asList(new Object[] {ProductionProcessState.WAITING}),
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
                Arrays.asList(new Object[] {ProductionProcessState.WAITING}),
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
                Arrays.asList(new Object[] {ProductionProcessState.WAITING}),
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
                Arrays.asList(new Object[] {ProductionProcessState.WAITING}),
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
                Arrays.asList(new Object[] {ProductionProcessState.WAITING}),
                DocumentMessage.MessageType.PROCESS_DATA_RES_OUT_OTHER,
                message -> {
                    if (productionProcess == null) {
                        return;
                    }

                    productionProcess.otherOutput()
                            .add(message.getDocument());
                });

	}

	private void sendProcessData(/* process data parameters here */){
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
        config.getConfiguration().forEach(linkConfigEntry -> {
            switch (linkConfigEntry.getAgentType()) {
                case BATCH_INPUT:
                    switch (linkConfigEntry.getOperationType()) {
                        case LINK:
                            this.inputBatchesAgents.add(linkConfigEntry.getAgentIdentifier());
                            break;
                        case UNLINK:
                            this.inputBatchesAgents.remove(linkConfigEntry.getAgentIdentifier());
                            break;
                    }
                    break;

                case BATCH_OUTPUT:
                    switch (linkConfigEntry.getOperationType()) {
                        case LINK:
                            this.outputBatchesAgents.add(linkConfigEntry.getAgentIdentifier());
                            break;
                        case UNLINK:
                            this.outputBatchesAgents.remove(linkConfigEntry.getAgentIdentifier());
                            break;
                    }
                    break;

                case INTERFACE:
                    switch (linkConfigEntry.getOperationType()) {
                        case LINK:
                            this.allSensors.add(linkConfigEntry.getAgentIdentifier());
                            break;
                        case UNLINK:
                            this.allSensors.remove(linkConfigEntry.getAgentIdentifier());
                            break;
                    }
                    break;

                case PERSISTENCE:
                    switch (linkConfigEntry.getOperationType()) {
                        case LINK:
                            persistenceAgent = linkConfigEntry.getAgentIdentifier();
                            break;
                        case UNLINK:
                            persistenceAgent = null;
                            break;
                    }
                    break;
            }
        });
    }

	private void addSensorConfirmation(AgentIdentifier sender, Runnable allConfirmedCallback) {
        if (!allSensors.contains(sender)) {
            return; // That sensor is not linked with this process
        }

        if (!confirmedSensors.add(sender)) {
            return; // That sensor has already been declared ready
        }

        if (allSensors.size() != confirmedSensors.size()) {
            return; // There are some sensors that are not confirmed yet
        }

        allConfirmedCallback.run();
    }

    private void clearConfirmation() {
        confirmedSensors.clear();
    }
}
