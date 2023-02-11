package davidcardd.chatAppbackend.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MessageSerializer extends StdSerializer<Message> {
    public MessageSerializer() {
        this(null);
    }

    public MessageSerializer(Class<Message> m) {
        super(m);
    }

    @Override
    public void serialize(Message message, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("body", message.getBody());
        jgen.writeStringField("timeSent", message.getTimeSent().toString());
        jgen.writeBooleanField("seen", message.isSeen());
        jgen.writeStringField("sender", message.getSender().getNickname());
        jgen.writeStringField("receiver", message.getReceiver().getNickname());
        jgen.writeEndObject();
    }
}
