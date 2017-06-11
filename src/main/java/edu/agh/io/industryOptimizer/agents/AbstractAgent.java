package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.MessageWrapper;
import edu.agh.io.industryOptimizer.messaging.messages.Messages;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractAgent extends Agent {
    private static final Logger log = Logger.getLogger(AbstractAgent.class.getName());

    protected final void setup() {
        setupCallbacks();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = getAgent().receive();
                if (mesg == null) {
                    return;
                }

                try {
                    byte[] received = mesg.getByteSequenceContent();

//                    log.debug("RECEIVED MESSAGE");

                    MessageWrapper message = Messages.fromBytes(received);

                    addBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            executeCallbacks(message.getMessage());
                        }
                    });
                } catch (UnreadableException | IOException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    log.warn("Received unknown message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                AbstractAgent.this.onStart();
            }
        });

        try {
            DFAgentDescription description = new DFAgentDescription();
            description.setName(getAID());

            ServiceDescription service = new ServiceDescription();
            service.setName(agentType().toString());
            service.setType(agentServiceType());

            description.addServices(service);

            DFService.register(this, description);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    protected final void sendMessage(String receiverName, Message message) throws IOException {
        assert receiverName != null;
        assert message != null;

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

        msg.addReceiver(new AID(receiverName, AID.ISLOCALNAME));

        msg.setByteSequenceContent(
                Messages.toBytes(new MessageWrapper(message))
        );

        addBehaviour(new OneShotBehaviour() {
            public void action() {
                send(msg);
            }
        });
    }

    protected final String getMyId() {
        return getAID().getName();
    }

    protected void onStart() {
    }

    protected abstract void executeCallbacks(Message message);

    protected abstract void setupCallbacks();

    protected abstract AgentType agentType();

    protected String agentServiceType() {
        return "general";
    }
}
