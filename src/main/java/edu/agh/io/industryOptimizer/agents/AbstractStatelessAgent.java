package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.util.SynchronizedCallbacksUtility;

public abstract class AbstractStatelessAgent extends AbstractAgent {
    private final CallbacksUtility utility = new SynchronizedCallbacksUtility();

    protected final CallbacksUtility getUtility() {
        return utility;
    }

    @Override
    protected void executeCallbacks(Message message) {
        utility.executeCallbacks(message.getMessageType(), message);
    }

    @Override
    protected final void setupCallbacks() {
        setupCallbacksStateless(utility);
    }

    protected abstract void setupCallbacksStateless(CallbacksUtility utility);
}
