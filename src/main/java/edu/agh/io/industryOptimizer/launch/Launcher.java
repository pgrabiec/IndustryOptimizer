package edu.agh.io.industryOptimizer.launch;

import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import jade.core.*;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
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
                "interface2",
                "edu.agh.io.industryOptimizer.agents.impl.interfaces.ConsoleSensor",
                new Object[0],
                containerController
        );

        launchAgent(
                "process1",
                "edu.agh.io.industryOptimizer.agents.impl.processes.ProductionProcessAgentImpl",
                new Object[] {
                        "process_type_1",
                        "process1"
                },
                containerController
        );

        launchAgent(
                "process2",
                "edu.agh.io.industryOptimizer.agents.impl.processes.ProductionProcessAgentImpl",
                new Object[] {
                        "process_type_1",
                        "process2"
                },
                containerController
        );

        launchAgent(
                "linking",
                "edu.agh.io.industryOptimizer.agents.impl.other.LinkingAgent",
                new Object[] {},
                containerController
        );

//        launchAgent(
//                "tmp",
//                "edu.agh.io.industryOptimizer.launch.Launcher$InitAgent",
//                new Object[] {},
//                containerController
//        );
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

//    public static class InitAgent extends Agent {
//        @Override
//        protected void setup() {
//            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//            msg.addReceiver(new AID("process1", AID.ISLOCALNAME));
//            msg.setLanguage("English");
//            try {
//                msg.setContentObject(
//                        new LinkConfigMessage(
//                                Collections.singletonList(new LinkConfigEntry(
//                                        OperationType.LINK,
//                                        AgentType.INTERFACE,
//                                        "interface1"
//                                )),
//                                MessageType.LINK_CONFIG,
//                                getAID().getName())
//                );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            send(msg);
//
//
//
//            ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
//            msg2.addReceiver(new AID("interface1", AID.ISLOCALNAME));
//            msg2.setLanguage("English");
//            try {
//                msg2.setContentObject(
//                        new LinkConfigMessage(
//                                Collections.singletonList(new LinkConfigEntry(
//                                        OperationType.LINK,
//                                        AgentType.PROCESS,
//                                        "process1"
//                                )),
//                                MessageType.LINK_CONFIG,
//                                getAID().getName())
//                );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            send(msg2);
//
//            doDelete();
//        }
//    }
}
