package edu.agh.io.industryOptimizer.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageWrapper {
    @JsonProperty("message")
    private final Message message;

    @JsonCreator
    public MessageWrapper(
            @JsonProperty("message") Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
