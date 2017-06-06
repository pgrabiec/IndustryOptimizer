package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import org.bson.Document;


public class DocumentMessage extends AbstractMessage {
    private final Document document;

    public DocumentMessage(Object messageType, AgentIdentifier sender, Document document) {
        super(messageType, sender);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public enum MessageType {
        PROCESS_READY,
        PROCESS_FORCE_FINALIZE,
        ANALYSIS_REQUEST,
        ANALYSIS_RESPONSE,
        DEDUCE_REQUEST,
        BATCH_PRODUCED,
        BATCH_LAST,
        PROCESS_DATA_RESPONSE,
        PROCESS_START,
        BATCH_NEW,
        OPTIMIZE_RESPONSE,
        ALGORITHMS,
        DATA_REQUEST,
        OPTIMIZE_REQUEST,
        BATCH_DATA,
        PROCESS_STOP,
        BATCH_DATA_RESPONSE,
        PROCESS_INIT,
        PROCESS_FINALIZE,
        DEDUCE_RESPONSE,
        BATCH_DATA_REQUEST,
        DATA_RESPONSE,
        PROCESS_DATA_REQUEST,
        PROCESS_FINISHED,
        PROCESS_DATA,
        PROCESS_DATA_PARAM_CONTROL,
        PROCESS_DATA_PARAM_OUT,
        PROCESS_DATA_RES_IN,
        PROCESS_DATA_RES_IN_OTHER,
        PROCESS_DATA_RES_OUT,
        PROCESS_DATA_RES_OUT_OTHER
    }
}
