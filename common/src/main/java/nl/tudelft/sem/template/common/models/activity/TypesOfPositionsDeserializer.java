package nl.tudelft.sem.template.common.models.activity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TypesOfPositionsDeserializer extends StdDeserializer<TypesOfPositions> {

    public TypesOfPositionsDeserializer() {
        this(null);
    }

    public TypesOfPositionsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TypesOfPositions deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        return TypesOfPositions.valueOf(value.toUpperCase());
    }
}
