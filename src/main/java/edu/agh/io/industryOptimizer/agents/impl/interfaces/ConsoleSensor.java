package edu.agh.io.industryOptimizer.agents.impl.interfaces;

import edu.agh.io.industryOptimizer.agents.impl.InterfaceAgent;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import org.bson.Document;

import java.io.IOException;
import java.util.HashMap;

public class ConsoleSensor extends InterfaceAgent {
    @Override
    protected void preSetup() {}

    @Override
    protected void waiting() {
        System.out.println("Waiting");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
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
    protected void initialize() {
        System.out.println("Initializing - ready in 1,5 s");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            sendMessageToProcess(new DocumentMessage(
                    DocumentMessage.MessageType.PROCESS_READY,
                    getMyId(),
                    new Document()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void execute() {
        for (int i=0; i<10; i++) {
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
                    DocumentMessage.MessageType.PROCESS_FINISHED,
                    getMyId(),
                    new Document()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalizing() {
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
                    DocumentMessage.MessageType.PROCESS_FINALIZE,
                    getMyId(),
                    new Document())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
