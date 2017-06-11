package edu.agh.io.industryOptimizer.messaging.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.agh.io.industryOptimizer.messaging.AbstractMessage;

import java.util.*;

public class LinkConfigMessage extends AbstractMessage {
    @JsonProperty("configuration")
    private final Collection<LinkConfigEntry> configuration;

    @JsonCreator
    public LinkConfigMessage(
            @JsonProperty("configuration") Collection<LinkConfigEntry> configuration,
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("sender") String sender) {
        super(messageType, sender);
        this.configuration = configuration;
    }

    public Collection<LinkConfigEntry> getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "LinkConfigMessage{" +
                "configuration=" + configuration +
                '}';
    }
}
