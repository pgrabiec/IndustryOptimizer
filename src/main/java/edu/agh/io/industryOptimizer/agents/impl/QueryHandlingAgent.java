package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.ResultMessage;
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
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                ResultMessage.class,
                MessageType.ALGORITHMS,
                message -> {
                    //Show algorithms
                }
        );

        utility.addCallback(
                ResultMessage.class,
                MessageType.OPTIMIZE_RESPONSE,
                message -> {
                    //Show optimization results
                }
        );

        utility.addCallback(
                ResultMessage.class,
                MessageType.ANALYSIS_RESPONSE,
                message -> {
                    //Show analysis results
                }
        );

        utility.addCallback(
                ResultMessage.class,
                MessageType.DATA_RESPONSE,
                message -> {
                    //Show data
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
