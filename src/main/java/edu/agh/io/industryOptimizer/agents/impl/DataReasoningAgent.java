package edu.agh.io.industryOptimizer.agents.impl;

import com.sun.istack.internal.NotNull;
import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;

public abstract class DataReasoningAgent extends AbstractStatelessAgent {
    @Override
    protected final void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                DocumentMessage.class,
                MessageType.DEDUCE_REQUEST,
                message -> {

                    Document result = deduce(message);

                    if (result == null) {
                        return;
                    }

                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        MessageType.DEDUCE_RESPONSE,
                                        getMyId(),
                                        result));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );


    }

    protected abstract Document deduce(@NotNull DocumentMessage request);

    @Override
    protected final AgentType agentType() {
        return AgentType.REASONING;
    }
}
