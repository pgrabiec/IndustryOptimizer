package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class PersistenceAgent extends AbstractAgent {
    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.BATCH_DATA,
                message -> {
                    // store data
                    System.out.println("Received batch data.");
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.PROCESS_DATA,
                message -> {
                    // store data
                    System.out.println("Received process data.");
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.BATCH_DATA_REQUEST,
                message -> {
                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        DocumentMessage.MessageType.BATCH_DATA_RESPONSE,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.PROCESS_DATA_REQUEST,
                message -> {
                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        DocumentMessage.MessageType.PROCESS_DATA_RESPONSE,
                                        getMyId(),
                                        new Document()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
