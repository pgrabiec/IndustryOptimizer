package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.RequestMessage;
import edu.agh.io.industryOptimizer.messaging.messages.ResultMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;

import java.io.IOException;

/**
 * Created by Tomasz on 02.06.2017.
 */
public class DataAnalysisAgent extends AbstractAgent {
    private final int SECONDS = 20;

    @Override
    protected void setupImpl(CallbacksUtility utility) {

        utility.addCallback(
                RequestMessage.class,
                MessageType.ANALYSIS_REQUEST,
                message -> {
                    //Analyse goes here
                    try {
                        Thread.sleep(SECONDS * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        sendMessage(message.getSender(),
                                new ResultMessage(MessageType.ANALYSIS_RESPONSE, getMyId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

    }
}
