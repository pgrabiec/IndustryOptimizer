package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.util.AgentIdApplier;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;


public abstract class ProductBatchAgent extends AbstractStatelessAgent {
    private String persistenceAgent;
    private BatchIdentifier batchId;

    @Override
    protected final void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                LinkConfigMessage.class,
                MessageType.LINK_CONFIG,
                this::applyLinkConfig
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.BATCH_LAST,
                message -> {
                    try {
                        Document document = new Document();

                        if (batchId != null) {
                            document.put("id", batchId.id().toString());
                        }

                        sendMessage(message.getSender(), new DocumentMessage(
                                MessageType.BATCH_LAST,
                                getMyId(),
                                document));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.BATCH_PRODUCED,
                message -> {
                    try {
                        String batchId = message.getDocument().getString("id");

                        if (batchId != null) {
                            this.batchId = new BatchIdentifier(new ObjectId(batchId));
                            onBatchProduced(this.batchId);
                        }
                    } catch (ClassCastException e) {
                        return; // no id tag
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.BATCH_DATA,
                message -> {
                    try {
                        sendMessage(persistenceAgent, new DocumentMessage(
                                MessageType.BATCH_DATA,
                                getMyId(),
                                message.getDocument()));

                        onBatchDataReceived(message.getDocument());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void applyLinkConfig(LinkConfigMessage config) {
        config.getConfiguration().forEach(linkConfigEntry -> {
            new AgentIdApplier()

                    .callback(AgentType.PERSISTENCE, id -> {
                        this.persistenceAgent = id;
                        if (id != null) {
                            onPersistenceLinked();
                        } else {
                            onPersistenceUnlinked();
                        }
                    })

                    .execute(linkConfigEntry);
        });
    }

    protected void onPersistenceLinked() {
    }

    protected void onPersistenceUnlinked() {
    }

    protected void onBatchProduced(BatchIdentifier batch) {
    }

    protected void onBatchDataReceived(Document data) {
    }

    @Override
    protected final AgentType agentType() {
        return AgentType.BATCH;
    }
}
