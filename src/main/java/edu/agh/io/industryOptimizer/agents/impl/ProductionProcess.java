package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.messages.*;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import edu.agh.io.industryOptimizer.model.data.Data;
import jade.core.behaviours.OneShotBehaviour;
import org.bson.Document;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

public class ProductionProcess extends AbstractStatefulAgent {
    private final Set<AgentIdentifier> allSensors = new HashSet<>();
    private final Set<AgentIdentifier> confirmedSensors = new HashSet<>();

    private final Map<ProductionProcessState, List<Data>> dataMap = new HashMap<>();

    private final List<Data> initData = new ArrayList<>();
    private final List<Data> execData = new ArrayList<>();
    private final List<Data> finalData = new ArrayList<>();

    private final ArrayList<LinkConfigMessage> configLinksPending = new ArrayList<>();

    private final Set<AgentIdentifier> inputBatchesAgents = new HashSet<>();
    private final Set<AgentIdentifier> outputBatchesAgents = new HashSet<>();

    private AgentIdentifier persistenceAgent = null;

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
    	// WAITING
        dataMap.put(ProductionProcessState.INITIALIZING, initData);
        dataMap.put(ProductionProcessState.EXECUTING, execData);
        dataMap.put(ProductionProcessState.FINALIZING, finalData);

		utility.addCallback(
            DocumentMessage.class,
            ProductionProcessState.WAITING,
                DocumentMessage.MessageType.PROCESS_INIT,
            message -> {
                sendToAllSensors(new DocumentMessage(
                        DocumentMessage.MessageType.PROCESS_INIT,
                        getMyId(),
                        new Document()
                ));
                setState(ProductionProcessState.INITIALIZING);
        });

        utility.addCallback(
            LinkConfigMessage.class,
            ProductionProcessState.WAITING,
            MessageType.LINK_CONFIG,
            this::applyLinkConfig);

        utility.addCallbackExcept(
            LinkConfigMessage.class,
            Arrays.asList(new ProductionProcessState[] {
                    ProductionProcessState.WAITING
            }),
            MessageType.LINK_CONFIG,
            configLinksPending::add);

        // INITIALIZING

        utility.addCallback(
            ControlMessage.class,
            ProductionProcessState.INITIALIZING,
            MessageType.PROCESS_READY,
            message -> addSensorConfirmation(
                message.getSender(),
                () -> {
                    clearConfirmation();
                    setState(ProductionProcessState.EXECUTING);
                    sendToAllSensors(
                        new ControlMessage(
                            MessageType.PROCESS_START,
                            getMyId())
                    );
                }
            ));

        utility.addCallback(
            DataMessage.class,
            ProductionProcessState.INITIALIZING,
            MessageType.PROCESS_DATA,
            this::handleData);

        utility.addCallback(
            ControlMessage.class,
            ProductionProcessState.INITIALIZING,
            MessageType.PROCESS_START,
            message -> {
                clearConfirmation();
                setState(ProductionProcessState.EXECUTING);
                sendToAllSensors(
                    new ControlMessage(
                        MessageType.PROCESS_START,
                        getMyId())
                );
            });

        utility.addCallback(
            BatchIdMessage.class,
            ProductionProcessState.INITIALIZING,
            MessageType.BATCH_LAST,
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
            BatchIdMessage.class,
            ProductionProcessState.INITIALIZING,
            MessageType.BATCH_NEW,
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
            DataMessage.class,
            ProductionProcessState.EXECUTING,
            MessageType.PROCESS_DATA,
            this::handleData);

        utility.addCallback(
            ControlMessage.class,
            ProductionProcessState.EXECUTING,
            MessageType.PROCESS_FINISHED,
            message -> addSensorConfirmation(
                message.getSender(),
                () -> {
                    clearConfirmation();
                    setState(ProductionProcessState.FINALIZING);
                    sendToAllSensors(
                        new ControlMessage(
                            MessageType.PROCESS_STOP,
                            getMyId())
                    );
                }
            ));

        utility.addCallback(
            ControlMessage.class,
            ProductionProcessState.EXECUTING,
            MessageType.PROCESS_STOP,
            message -> {
                clearConfirmation();
                setState(ProductionProcessState.FINALIZING);
                sendToAllSensors(
                    new ControlMessage(
                        MessageType.PROCESS_STOP,
                        getMyId())
                );
            });

        // FINALIZING

        utility.addCallback(
                DataMessage.class,
                ProductionProcessState.FINALIZING,
                MessageType.PROCESS_DATA,
                this::handleData);

        utility.addCallback(
                ControlMessage.class,
                ProductionProcessState.FINALIZING,
                MessageType.PROCESS_FINALIZE,
                message -> addSensorConfirmation(
                    message.getSender(),
                    () -> {
                        clearConfirmation();
                        setState(ProductionProcessState.EXPANDING);
                        sendToAllSensors(
                            new ControlMessage(
                                MessageType.PROCESS_FINALIZE,
                                getMyId())
                        );
                    }
                ));

        utility.addCallback(
                ControlMessage.class,
                ProductionProcessState.FINALIZING,
                MessageType.PROCESS_FORCE_FINALIZE,
                message -> {
                    clearConfirmation();
                    setState(ProductionProcessState.EXPANDING);
                    sendToAllSensors(
                        new ControlMessage(
                            MessageType.PROCESS_FINALIZE,
                            getMyId())
                    );
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setState(ProductionProcessState.EXPANDING);
                    sendData(/* process data parameters here */);
                    setState(ProductionProcessState.WAITING);
                });

        // EXPANDING
        // no message handling

	}

	private void sendData(/* process data parameters here */){
        try {
            sendMessage(persistenceAgent, new ProcessDataMessage(MessageType.PROCESS_DATA, getMyId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void handleData(DataMessage message) {
        if (persistenceAgent == null) {
            System.out.println("Received process data but no persistence present");
            dataMap.get(getProcessState()).add(message.getData());
            return;
        }

        try {
            sendMessage(persistenceAgent, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            if (linkConfigEntry.getAgentType()
                    .equals(AgentType.BATCH)) {}

                // TODO: HOW TO DIFFER INPUT BATCHES FROM OUTPUT BATCHES?



            if (linkConfigEntry.getAgentType()
                        .equals(AgentType.INTERFACE)) {
                switch (linkConfigEntry.getOperationType()) {
                    case LINK:
                        allSensors.add(linkConfigEntry.getAgentIdentifier());
                        break;
                    case UNLINK:
                        allSensors.remove(linkConfigEntry.getAgentIdentifier());
                        break;
               }
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
