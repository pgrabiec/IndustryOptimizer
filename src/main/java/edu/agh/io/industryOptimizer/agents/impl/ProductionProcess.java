package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatefulAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.ControlMessage;
import edu.agh.io.industryOptimizer.messaging.messages.DataMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import jade.core.behaviours.OneShotBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProductionProcess extends AbstractStatefulAgent {
    private final Set<AgentIdentifier> allSensors = new HashSet<>();
    private final Set<AgentIdentifier> confirmedSensors = new HashSet<>();

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

		utility.addCallback(ControlMessage.class, ProductionProcessState.WAITING, MessageType.PROCESS_INIT,
                message -> {
                    sendToAllSensors(new ControlMessage(
                    		MessageType.PROCESS_INIT,
							getMyId()
					));
                    setState(ProductionProcessState.INITIALIZING);
        });

        utility.addCallback(LinkConfigMessage.class, ProductionProcessState.WAITING, MessageType.LINK_CONFIG,
				this::applyLinkConfig);

        utility.addCallbackExcept(LinkConfigMessage.class,
                Arrays.asList(new ProductionProcessState[] {
						ProductionProcessState.WAITING
				}),
        		MessageType.LINK_CONFIG,
				configLinksPending::add);

        // INITIALIZING

        utility.addCallback(ControlMessage.class, ProductionProcessState.INITIALIZING, MessageType.PROCESS_READY,
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

        utility.addCallback(DataMessage.class, ProductionProcessState.INITIALIZING, MessageType.PROCESS_DATA,
                this::handleData);

        utility.addCallback(ControlMessage.class, ProductionProcessState.INITIALIZING, MessageType.PROCESS_START,
                message -> {
            clearConfirmation();
            setState(ProductionProcessState.EXECUTING);
            sendToAllSensors(
                    new ControlMessage(
                            MessageType.PROCESS_START,
                            getMyId())
            );
        });

//        TODO
//
//        utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.BATCH_LAST, message -> {
//
//        });
//        utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.BATCH_LAST, message -> {
//
//        });
//
//        // EXECUTING
//
//        utility.addCallback(ProductionProcessState.EXECUTING, MessageType.PROCESS_DATA, message -> {
//            //DATA
//        });
//        utility.addCallback(ProductionProcessState.EXECUTING, MessageType.PROCESS_FINISHED, message -> {
//
//        });
//        utility.addCallback(ProductionProcessState.EXECUTING, MessageType.PROCESS_STOP, message -> {
//            sendToAllSensors(MessageType.PROCESS_STOP, null);
//            state = ProductionProcessState.FINALIZING;
//        });
//
//        // FINALIZING
//
//        utility.addCallback(ProductionProcessState.FINALIZING, MessageType.PROCESS_DATA, message -> {
//            //DATA
//        });
//        utility.addCallback(ProductionProcessState.FINALIZING, MessageType.PROCESS_FINALIZE, message -> {
//            agentMessagesCounter++;
//            if(agentMessagesCounter == sensorsList.size()){
//                agentMessagesCounter = 0;
//                sendToAllSensors(MessageType.PROCESS_FINALIZE, null);
//                state = ProductionProcessState.EXPANDING;
//                System.out.println("Sending data to Product Batch Agent");
//                sendMessage(batchAgent, MessageType.BATCH_PRODUCED, "!!!!!!!!!!!!!!!!!!!!!!TODO");
//                state = ProductionProcessState.WAITING;
//                checkLinkConfigs();
//            }
//        });
//        utility.addCallback(ProductionProcessState.FINALIZING, MessageType.PROCESS_FORCE_FINALIZE, message -> {
//            sendToAllSensors(MessageType.PROCESS_FINALIZE, null);
//            state = ProductionProcessState.EXPANDING;
//            System.out.println("Sending data to Product Batch Agent");
//            sendMessage(batchAgent, MessageType.BATCH_PRODUCED, "!!!!!!!!!!!!!!!!!!!!!!TODO");
//            state = ProductionProcessState.WAITING;
//            checkLinkConfigs();
//        });
//
//        // EXPANDING
//

	}

	private void handleData(DataMessage message) {
        if (persistenceAgent == null) {
            System.out.println("Received process data but no persistence present");
            return;
        }

        try {
            sendMessage(persistenceAgent, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyLinkConfig(LinkConfigMessage message) {

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
