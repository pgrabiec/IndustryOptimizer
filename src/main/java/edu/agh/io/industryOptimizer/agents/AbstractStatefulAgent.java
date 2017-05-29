package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.CallbacksUtilityImpl;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.MessageVisitor;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class AbstractStatefulAgent extends Agent implements MessageVisitor {
    private final CallbacksUtility utility = new CallbacksUtilityImpl();

    protected void setup() {
        setupImpl(utility);

        AbstractStatefulAgent thisAgent = this;

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = getAgent().receive();
                if (mesg != null) {
                    try {
                        ((Message) mesg).accept(thisAgent);
                    } catch (ClassCastException e) {
                        System.err.println("Received unknown message");
                    }
                }
            }
        });
    }

    /**
     * For initializing the CallbacksUtility
     * */
    protected abstract void setupImpl(CallbacksUtility utility);

    /**
     * Provides the current state
     * */
    protected abstract ProductionProcessState getProcessState();
}
