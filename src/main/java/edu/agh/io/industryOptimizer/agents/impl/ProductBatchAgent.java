package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.BatchDataMessage;
import edu.agh.io.industryOptimizer.messaging.messages.BatchIdMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;

import java.io.IOException;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class ProductBatchAgent extends AbstractAgent {
    private AgentIdentifier persistenceId;
    private AgentIdentifier persistenceAgent;
    private BatchIdentifier batchId;

    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                LinkConfigMessage.class,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                BatchIdMessage.class,
                MessageType.BATCH_LAST,
                message -> {
                    try {
                        sendMessage(message.getSender(), new BatchIdMessage(
                                MessageType.BATCH_LAST,
                                getMyId(),
                                batchId
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                BatchIdMessage.class,
                MessageType.BATCH_PRODUCED,
                message -> {
                    try {
                        sendMessage(message.getSender(), new BatchIdMessage(
                                MessageType.BATCH_PRODUCED,
                                getMyId(),
                                batchId
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                BatchDataMessage.class,
                MessageType.BATCH_DATA,
                message -> {
                    try {
                        sendMessage(persistenceAgent, new BatchDataMessage(
                                MessageType.BATCH_DATA,
                                getMyId()
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            if (!linkConfigEntry.getAgentType()
                    .equals(AgentType.PERSISTENCE)) {
                return;
            }

            switch (linkConfigEntry.getOperationType()) {
                case LINK:
                    this.persistenceId = linkConfigEntry.getAgentIdentifier();
                    break;
                case UNLINK:
                    this.persistenceId = null;
                    break;
            }
        });
    }
}
