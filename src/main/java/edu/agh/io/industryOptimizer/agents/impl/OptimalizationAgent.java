package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.*;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;

import java.io.IOException;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class OptimalizationAgent extends AbstractAgent {

    private AgentIdentifier persistenceAgent;
    private AgentIdentifier algorithmsAgent;
    private AgentIdentifier queryAgent;

    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                LinkConfigMessage.class,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                RequestMessage.class,
                MessageType.ALGORITHMS,
                message -> {
                    //get algorithms
                    queryAgent = message.getSender();
                    try {
                        sendMessage(message.getSender(),
                                new ResultMessage(MessageType.ALGORITHMS, getMyId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                RequestMessage.class,
                MessageType.OPTIMIZE_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(algorithmsAgent,
                                new ResultMessage(MessageType.DEDUCE_REQUEST, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                RequestMessage.class,
                MessageType.ANALYSIS_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(algorithmsAgent,
                                new ResultMessage(MessageType.ANALYSIS_REQUEST, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                RequestMessage.class,
                MessageType.DATA_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(persistenceAgent,
                                new ResultMessage(MessageType.ANALYSIS_REQUEST, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );

        utility.addCallback(
                DataMessage.class,
                MessageType.BATCH_DATA_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new ResultMessage(MessageType.DATA_RESPONSE, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DataMessage.class,
                MessageType.PROCESS_DATA_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new ResultMessage(MessageType.DATA_RESPONSE, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                ResultMessage.class,
                MessageType.ANALYSIS_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new ResultMessage(MessageType.ANALYSIS_RESPONSE, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                ResultMessage.class,
                MessageType.DEDUCE_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new ResultMessage(MessageType.OPTIMIZE_RESPONSE, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            try {
                if (linkConfigEntry.getAgentType()
                        .equals(AgentType.PERSISTENCE)) {
                    switch (linkConfigEntry.getOperationType()) {
                        case LINK:
                            persistenceAgent = linkConfigEntry.getAgentIdentifier();
                            break;
                        case UNLINK:
                            persistenceAgent = null;
                            break;
                    }
                }

                if (linkConfigEntry.getAgentType()
                        .equals(AgentType.ANALYSIS)) {
                    switch (linkConfigEntry.getOperationType()) {
                        case LINK:
                            algorithmsAgent = linkConfigEntry.getAgentIdentifier();
                            break;
                        case UNLINK:
                            algorithmsAgent = null;
                            break;
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Wrong agent type");
            }
        });
    }
}
