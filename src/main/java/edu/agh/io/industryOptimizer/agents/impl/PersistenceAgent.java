package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;

public abstract class PersistenceAgent extends AbstractStatelessAgent {

    @Override
    protected final void setupCallbacksStateless(CallbacksUtility utility) {
        utility.addCallback(
                DocumentMessage.class,
                MessageType.BATCH_DATA,
                message -> {
                    storeBatchData(message.getDocument());
//                    System.out.println("Received batch data.");
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.PROCESS_DATA,
                message -> {
                    storeProcessData(message.getDocument());
//                    System.out.println("Received process data.");
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.BATCH_DATA_REQUEST,
                message -> {
                    try {
                        Document batchData = retrieveBatchData(message.getDocument());

                        if (batchData == null) {
                            batchData = new Document();
                        }

                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        MessageType.BATCH_DATA_RESPONSE,
                                        getMyId(),
                                        batchData));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                MessageType.PROCESS_DATA_REQUEST,
                message -> {
                    Document processData = retrieveProcessData(message.getDocument());

                    if (processData == null) {
                        processData = new Document();
                    }

                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        MessageType.PROCESS_DATA_RESPONSE,
                                        getMyId(),
                                        processData));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    protected abstract Document retrieveBatchData(Document request);

    protected abstract Document retrieveProcessData(Document request);

    protected abstract void storeBatchData(Document data);

    protected abstract void storeProcessData(Document data);


}
