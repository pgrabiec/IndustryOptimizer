package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;
import java.util.function.Consumer;

public class OptimalizationAgent extends AbstractStatelessAgent {

    private AgentIdentifier persistenceAgent;
    private AgentIdentifier algorithmsAgent;
    private AgentIdentifier queryAgent;

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            try {
                Consumer<AgentIdentifier> callback = agentIdentifier -> {
                    persistenceAgent = agentIdentifier;
                };
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
//                System.out.println("Wrong agent type");
            }
        });
    }

    @Override
    protected void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                LinkConfigMessage.class,
                LinkConfigMessage.MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ALGORITHMS,
                message -> {
                    //get algorithms
                    queryAgent = message.getSender();
                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        DocumentMessage.MessageType.ALGORITHMS,
                                        getMyId(),
                                        new Document()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.OPTIMIZE_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(algorithmsAgent,
                                new DocumentMessage(DocumentMessage.MessageType.DEDUCE_REQUEST,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ANALYSIS_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(algorithmsAgent,
                                new DocumentMessage(
                                        DocumentMessage.MessageType.ANALYSIS_REQUEST,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.DATA_REQUEST,
                message -> {
                    queryAgent = message.getSender();
                    try {
                        sendMessage(persistenceAgent,
                                new DocumentMessage(
                                        DocumentMessage.MessageType.ANALYSIS_REQUEST,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.BATCH_DATA_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        DocumentMessage.MessageType.DATA_RESPONSE,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.PROCESS_DATA_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        DocumentMessage.MessageType.DATA_RESPONSE,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ANALYSIS_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        DocumentMessage.MessageType.ANALYSIS_RESPONSE,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.DEDUCE_RESPONSE,
                message -> {
                    try {
                        sendMessage(queryAgent,
                                new DocumentMessage(
                                        DocumentMessage.MessageType.OPTIMIZE_RESPONSE,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
