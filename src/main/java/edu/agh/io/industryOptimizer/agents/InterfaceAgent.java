package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.CallbacksUtility;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class InterfaceAgent extends Agent {
    private final CallbacksUtility utility = new CallbacksUtility();

    protected void setup() {

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = myAgent.receive();
                if (mesg != null) {

                }
            }
        });
    }

    protected void takeDown() {

    }

    public class getMessages extends CyclicBehaviour {
        public void action() {
            ACLMessage mesg = myAgent.receive();
            if (mesg != null) {

            }
        }
    }
}
