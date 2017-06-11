package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.Message;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public abstract class AbstractAgent extends Agent {

    protected final void setup() {
        setupCallbacks();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = getAgent().receive();
                if (mesg != null) {
                    try {
                        Message message = (Message) mesg.getContentObject();
                        addBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {
                                executeCallbacks(message);
                            }
                        });
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    } catch (ClassCastException e) {
                        System.err.println("Received unknown message");
                    }
                }
            }
        });

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                AbstractAgent.this.onStart();
            }
        });
    }

    protected void sendMessage(AgentIdentifier receiver, Message message) throws IOException {
        assert receiver != null;
        assert message != null;

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

    protected abstract void executeCallbacks(Message message);

    protected abstract void setupCallbacks();

    protected void onStart() {}
}
