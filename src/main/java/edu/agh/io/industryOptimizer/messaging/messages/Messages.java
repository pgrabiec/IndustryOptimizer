package edu.agh.io.industryOptimizer.messaging.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.agh.io.industryOptimizer.messaging.MessageWrapper;
import org.apache.log4j.Logger;

public class Messages {
    private static final Logger log = Logger.getLogger(Messages.class.getName());

    public static byte[] toBytes(MessageWrapper message) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper().enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        byte[] bytes = mapper.writeValueAsBytes(message);

//        log.debug("MAPPING: " + new String(bytes));

        return bytes;
    }

    public static MessageWrapper fromBytes(byte[] bytes) throws Exception {
        ObjectMapper mapper = new ObjectMapper().enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

//        log.debug("Messages.fromBytes: " + new String(bytes));

        return mapper.readValue(bytes, MessageWrapper.class);
    }
}
