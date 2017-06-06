package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class QueryHandlingAgent extends AbstractAgent {
    private AgentIdentifier optimizationAgent;

    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                LinkConfigMessage.class,
                LinkConfigMessage.MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ALGORITHMS,
                message -> {
                    // TODO present algorithms
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.OPTIMIZE_RESPONSE,
                message -> {
                    // TODO present optimization results
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ANALYSIS_RESPONSE,
                message -> {
                    // TODO show analysis results
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.DATA_RESPONSE,
                message -> {
                    // TODO show data
                }
        );

    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {

            if (linkConfigEntry.getAgentType()
                    .equals(AgentType.OPTIMIZATION)) {
                switch (linkConfigEntry.getOperationType()) {
                    case LINK:
                        optimizationAgent = linkConfigEntry.getAgentIdentifier();
                        break;
                    case UNLINK:
                        optimizationAgent = null;
                        break;
                }
            }
        });
    }
}
