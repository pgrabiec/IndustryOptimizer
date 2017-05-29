package edu.agh.io.industryOptimizer.messaging.messages;

import edu.agh.io.industryOptimizer.messaging.DefaultMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import edu.agh.io.industryOptimizer.messaging.MessageVisitor;

/**
 * Created by Tomasz on 29.05.2017.
 */
public class DataMessage extends DefaultMessage{
    private String resource;
    private String value;
    private String unit;

    public String getResource() {
        return resource;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public DataMessage(MessageType messageType, String resource, String value, String unit) {
        super(messageType);
        this.resource = resource;
        this.value = value;
        this.unit = unit;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visitDataMessage(this);
    }
}
