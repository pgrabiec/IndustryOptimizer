package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class DataAnalysisAgent extends AbstractAgent {
    private final int SECONDS = 20;

    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                DocumentMessage.class,
                DocumentMessage.MessageType.ANALYSIS_REQUEST,
                message -> {
                    //Analyse goes here
                    try {
                        Thread.sleep(SECONDS * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        sendMessage(message.getSender(),
                                new DocumentMessage(
                                        DocumentMessage.MessageType.ANALYSIS_RESPONSE,
                                        getMyId(),
                                        new Document()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

    }
}
