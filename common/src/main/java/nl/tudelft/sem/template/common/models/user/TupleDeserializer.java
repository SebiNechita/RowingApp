package nl.tudelft.sem.template.common.models.user;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TupleDeserializer extends StdDeserializer<Tuple<LocalDateTime, LocalDateTime>> {

    static final long serialVersionUID = 1L;

    public TupleDeserializer() {
        this(null);
    }

    public TupleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Tuple<LocalDateTime, LocalDateTime> deserialize(JsonParser jsonParser,
                                                           DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String first = node.get("first").asText();
        String second = node.get("second").asText();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime firstDateTime = LocalDateTime.parse(first, formatter);
        LocalDateTime secondDateTime = LocalDateTime.parse(second, formatter);

        return new Tuple<>(firstDateTime, secondDateTime);
    }
}
