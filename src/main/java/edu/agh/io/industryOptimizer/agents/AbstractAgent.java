package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtilityImpl;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public abstract class AbstractAgent extends Agent {
    private final CallbacksUtility utility = new CallbacksUtilityImpl();

    protected void setup() {
        setupImpl(utility);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = getAgent().receive();
                if (mesg != null) {
                    try {
                        Message message = (Message) mesg.getContentObject();
                        utility.executeCallbacks(message.getMessageType(), message);
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    } catch (ClassCastException e) {
                        System.err.println("Received unknown message");
                    }
                }
            }
        });
    }

    protected void sendMessage(AgentIdentifier receiver, Message message) throws IOException {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(receiver.id(), AID.ISLOCALNAME));
        msg.setLanguage("English");
        msg.setContentObject(message);

        addBehaviour(new OneShotBehaviour() {
            public void action() {
                send(msg);
            }
        } );
    }

    protected AgentIdentifier getMyId() {
        return new AgentIdentifierImpl(getAID().getName());
    }

    /**
     * For initializing the CallbacksUtility
     * */
    protected abstract void setupImpl(CallbacksUtility utility);
}
