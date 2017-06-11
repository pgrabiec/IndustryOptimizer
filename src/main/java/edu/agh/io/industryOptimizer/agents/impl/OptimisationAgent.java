package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.util.AgentIdApplier;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;

public abstract class OptimisationAgent extends AbstractStatelessAgent {
    private String persistenceAgent;
    private String algorithmsAgent;
    private String queryAgent;

    @Override
    protected final void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                LinkConfigMessage.class,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.ALGORITHMS,
                message -> {
                    Document algorithms = acquireAlgorithms();

                    if (algorithms == null) {
                        algorithms = new Document();
                    }

                    queryAgent = message.getSender();
                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        MessageType.ALGORITHMS,
                                        getMyId(),
                                        algorithms));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.OPTIMIZE_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(algorithmsAgent,
                                // Pass the query through
                                new DocumentMessage(MessageType.DEDUCE_REQUEST,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.ANALYSIS_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(algorithmsAgent,
                                new DocumentMessage(
                                        MessageType.ANALYSIS_REQUEST,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.DATA_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(persistenceAgent,
                                new DocumentMessage(
                                        MessageType.ANALYSIS_REQUEST,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.BATCH_DATA_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        MessageType.DATA_RESPONSE,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.PROCESS_DATA_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        MessageType.DATA_RESPONSE,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.ANALYSIS_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        MessageType.ANALYSIS_RESPONSE,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.DEDUCE_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        MessageType.OPTIMIZE_RESPONSE,
                                        getMyId(),
                                        message.getDocument()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            new AgentIdApplier()
                    .callback(AgentType.PERSISTENCE, id -> persistenceAgent = id)

                    .callback(AgentType.ANALYSIS, id -> algorithmsAgent = id)

                    .callback(AgentType.REASONING, id -> algorithmsAgent = id)

                    .execute(linkConfigEntry);
        });
    }

    protected abstract Document acquireAlgorithms();

    @Override
    protected final AgentType agentType() {
        return AgentType.OPTIMIZATION;
    }
}
