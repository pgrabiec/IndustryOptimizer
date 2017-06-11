package edu.agh.io.industryOptimizer.agents.impl.interfaces;

import edu.agh.io.industryOptimizer.agents.impl.AbstractInterfaceAgent;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.util.HashMap;

public class ConsoleSensor extends AbstractInterfaceAgent {
    private static final Logger log = Logger.getLogger(ConsoleSensor.class.getName());

    @Override
    protected void onStart() {
        log.debug("Pre setup");
    }

    @Override
    protected void onWaiting() {
        if (!isProcessAgentLinked()) {
            log.debug("Waiting - No agent");
            return;
        }

        log.debug("Waiting - initializing process in 1s");
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Interrupted - continue
            }

            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_INIT,
                    getMyId(),
                    new Document()
            ));

            log.debug("Sent init");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onInitializing() {
        log.debug("Initializing - ready in 1,5 s");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // Interrupted - continue
        }

        log.debug("Starting process");

        try {
            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_START,
                    getMyId(),
                    new Document()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onExecuting() {
        log.debug("Executing");
        for (int i=0; i<2; i++) {
            try {
                Document document = new Document();
                document.put("name", "power" + (System.currentTimeMillis() % 100));
                document.put("value", (System.currentTimeMillis() % 100));
                document.put("unit", "MWh");

                sendMessageToProcess(
                        new DocumentMessage(
                                DocumentMessage.MessageType.PROCESS_DATA_PARAM_CONTROL,
                                getMyId(),
                                document
                        ));
                log.debug("Param " + i);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_STOP,
                    getMyId(),
                    new Document()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinalizing() {
        log.debug("Finalizing");
        try {
            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_DATA_PARAM_OUT,
                    getMyId(),
                    new Document(
                            new HashMap<String, Object>(3) {{
                                put("name", "out_param1");
                                put("value", 142);
                                put("unit", "kg");
                            }}
                    )
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_FORCE_FINALIZE,
                    getMyId(),
                    new Document())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProcessAgentLinked() {
        try {
            log.debug("Process linked - sending init");

            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_INIT,
                    getMyId(),
                    new Document()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProcessAgentUnlinked() {
        log.debug("Production process unlinked");
    }
}
