package edu.agh.io.industryOptimizer.agents.impl;

import com.sun.istack.internal.NotNull;
import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;

public abstract class DataAnalysisAgent extends AbstractStatelessAgent {
    @Override
    protected final void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ANALYSIS_REQUEST,
                message -> {
                    try {

                        Document result = analyze(message);

                        if (result == null) {
                            return;
                        }

                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        DocumentMessage.MessageType.ANALYSIS_RESPONSE,
                                        getMyId(),
                                        result));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    protected abstract Document analyze(@NotNull DocumentMessage request);
}
