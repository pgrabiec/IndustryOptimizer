package edu.agh.io.industryOptimizer.agents.interfaces;

import edu.agh.io.industryOptimizer.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.ProductionProcessState;
import edu.agh.io.industryOptimizer.messaging.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.CallbacksUtilityImpl;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.Serializable;

public class InterfaceAgent extends Agent {
    private final CallbacksUtility utility = new CallbacksUtilityImpl();

    private ProductionProcessState state = ProductionProcessState.WAITING;
    private AgentIdentifier productionProcessId;


    protected void setup() {
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
                message -> {
                    state = ProductionProcessState.INITIALIZING;
                    initialize();
                }
        );

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = myAgent.receive();
                if (mesg != null) {
                    try {
                        handleMessage(mesg.getContentObject());
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

//    private void handleMessage(Message message) {
//        utility.getCallback(state, message.getMessageType())
//                .forEach(callback ->
//                        callback.messageReceived(message.getContent())
//                );
//    }

    private void handleMessage(Serializable contentObject) {
        System.err.println("Received unknown message");
    }

    private void initialize() {

    }
//
//    protected void takeDown() {
//
//    }
}
