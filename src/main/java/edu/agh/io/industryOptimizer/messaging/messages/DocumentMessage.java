package edu.agh.io.industryOptimizer.messaging.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;
import org.bson.Document;

import java.io.Serializable;


public class DocumentMessage extends AbstractMessage {
    @JsonProperty("document")
    private final Document document;

    @JsonCreator
    public DocumentMessage(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("sender") String sender,
            @JsonProperty("document") Document document) {
        super(messageType, sender);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
