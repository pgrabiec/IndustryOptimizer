package edu.agh.io.industryOptimizer.launch;

import edu.agh.io.industryOptimizer.agents.AgentIdentifierImpl;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import jade.core.*;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Launcher {
    public static void launch() throws StaleProxyException {
        ContainerController containerController = createContainer();

        launchAgent(
                "interface1",
                "edu.agh.io.industryOptimizer.agents.impl.interfaces.ConsoleSensor",
                new Object[0],
                containerController
        );

        launchAgent(
                "process1",
                "edu.agh.io.industryOptimizer.agents.impl.ProductionProcessAgent",
                new Object[] {
                        "process_type_1",
                        "process1"
                },
                containerController
        );

        launchAgent(
                "tmp",
                "edu.agh.io.industryOptimizer.launch.Launcher$InitAgent",
                new Object[] {},
                containerController
        );


    }

    private static ContainerController createContainer() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        return rt.createAgentContainer(p);
    }

    private static void launchAgent(String name, String className, Object[] args, ContainerController containerController) throws StaleProxyException {
        AgentController dummy = containerController.createNewAgent(name, className, args);
        dummy.start();
    }

    public static class InitAgent extends Agent {
        @Override
        protected void setup() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("process1", AID.ISLOCALNAME));
            msg.setLanguage("English");
            try {
                msg.setContentObject(
                        new LinkConfigMessage(
                                LinkConfigMessage.MessageType.LINK_CONFIG,
                                Collections.singletonList(new LinkConfigMessage.LinkConfigEntry(
                                        LinkConfigMessage.OperationType.LINK,
                                        AgentType.INTERFACE,
                                        new AgentIdentifierImpl("interface1")
                                )),
                                new AgentIdentifierImpl(getAID().getName())
                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            send(msg);



            ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
            msg2.addReceiver(new AID("interface1", AID.ISLOCALNAME));
            msg2.setLanguage("English");
            try {
                msg2.setContentObject(
                        new LinkConfigMessage(
                                LinkConfigMessage.MessageType.LINK_CONFIG,
                                Collections.singletonList(new LinkConfigMessage.LinkConfigEntry(
                                        LinkConfigMessage.OperationType.LINK,
                                        AgentType.PROCESS,
                                        new AgentIdentifierImpl("process1")
                                )),
                                new AgentIdentifierImpl(getAID().getName())
                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            send(msg2);
        }
    }
}
