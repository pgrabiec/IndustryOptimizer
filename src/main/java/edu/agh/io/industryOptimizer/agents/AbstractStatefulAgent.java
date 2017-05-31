package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtilityImpl;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class AbstractStatefulAgent extends AbstractAgent {
    private final StatefulCallbacksUtility utility = new StatefulCallbacksUtilityImpl();
    private ProductionProcessState state = ProductionProcessState.WAITING;

    protected void setup() {
        setupImpl(utility);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mesg = getAgent().receive();
                if (mesg != null) {
                    try {
                        Message message = (Message) mesg.getContentObject();
                        utility.executeCallbacks(state, message.getMessageType(), message);
                    } catch (UnreadableException e) {
                        e.printStackTrace();
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
    protected abstract void setupImpl(StatefulCallbacksUtility utility);

    protected void setState(ProductionProcessState state) {
        this.state = state;
    }

    protected ProductionProcessState getProcessState() {
        return state;
    }

    @Override
    protected void setupImpl(CallbacksUtility utility) {
        setupImpl(this.utility);
    }
}
