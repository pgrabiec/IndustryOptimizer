package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.util.AgentIdApplier;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

public abstract class QueryHandlingAgent extends AbstractStatelessAgent {
    private AgentIdentifier optimizationAgent;

    @Override
    protected final void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                LinkConfigMessage.class,
                LinkConfigMessage.MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ALGORITHMS,
                message -> onAlgorithmsResponse(message.getDocument())
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.OPTIMIZE_RESPONSE,
                message -> {
                    onOptimiseResponse(message.getDocument());
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ANALYSIS_RESPONSE,
                message -> {
                    onAnalysisResponse(message.getDocument());
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.DATA_RESPONSE,
                message -> {
                    onDataResponse(message.getDocument());
                }
        );
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {

            new AgentIdApplier()
                    .callback(AgentType.OPTIMIZATION, id -> {
                        if (id != null) {
                            optimizationAgent = id;
                            onOptimizationLinked(id);
                        } else {
                            optimizationAgent = null;
                            onOptimizationUnlinked();
                        }
                    })
                    .execute(linkConfigEntry);

        });
    }

    protected void onOptimizationUnlinked() {}

    protected void onOptimizationLinked(AgentIdentifier id) {}

    protected void onDataResponse(Document dataResponse) {}

    protected void onAnalysisResponse(Document analysisResponse) {}

    protected void onOptimiseResponse(Document optimiseResponse) {}

    protected void onAlgorithmsResponse(Document algorithmsResponse) {}
}
