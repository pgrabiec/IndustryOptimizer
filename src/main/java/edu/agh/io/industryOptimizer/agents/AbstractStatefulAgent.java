package edu.agh.io.industryOptimizer.agents;

import com.sun.istack.internal.NotNull;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.util.StatefulCallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.util.SynchronizedStatefulCallbacksUtility;

public abstract class AbstractStatefulAgent extends AbstractAgent {
    private final StatefulCallbacksUtility utility = new SynchronizedStatefulCallbacksUtility();
    private ProductionProcessState state = ProductionProcessState.WAITING;

    public AbstractStatefulAgent(ProductionProcessState initialState) {
        this.state = initialState;
    }

    public AbstractStatefulAgent() {
    }

    protected final void setProcessState(ProductionProcessState state) {
        this.state = state;
    }

    protected final ProductionProcessState getProcessState() {
        return state;
    }

    protected final StatefulCallbacksUtility getUtility() {
        return utility;
    }

    @Override
    protected final void executeCallbacks(Message message) {
        utility.executeCallbacks(state, message.getMessageType(), message);
    }

    @Override
    protected final void setupCallbacks() {
        setupCallbacksStateful(utility);
    }

    /**
     * For initializing the CallbacksUtility
     * */
    protected abstract void setupCallbacksStateful(@NotNull StatefulCallbacksUtility utility);
}
