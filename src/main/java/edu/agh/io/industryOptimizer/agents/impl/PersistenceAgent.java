package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.BatchDataMessage;
import edu.agh.io.industryOptimizer.messaging.messages.ProcessDataMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;

import java.io.IOException;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class PersistenceAgent extends AbstractAgent {
    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                BatchDataMessage.class,
                MessageType.BATCH_DATA,
                message -> {
                    // store data
                    System.out.println("Received batch data.");
                }
        );

        utility.addCallback(
                ProcessDataMessage.class,
                MessageType.PROCESS_DATA,
                message -> {
                    // store data
                    System.out.println("Received process data.");
                }
        );

        utility.addCallback(
                BatchDataMessage.class,
                MessageType.BATCH_DATA_REQUEST,
                message -> {
                    try {
                        sendMessage(message.getSender(),
                                new BatchDataMessage(MessageType.BATCH_DATA_RESPONSE, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        utility.addCallback(
                ProcessDataMessage.class,
                MessageType.PROCESS_DATA_REQUEST,
                message -> {
                    try {
                        sendMessage(message.getSender(),
                                new ProcessDataMessage(MessageType.PROCESS_DATA_RESPONSE, getMyId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
