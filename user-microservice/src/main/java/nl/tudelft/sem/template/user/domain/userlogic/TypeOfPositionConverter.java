package nl.tudelft.sem.template.user.domain.userlogic;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;


@Converter
public class TypeOfPositionConverter implements AttributeConverter<TypesOfPositions, String> {

    @Override
    public String convertToDatabaseColumn(TypesOfPositions type) {
        return type.toString();
    }

    @Override
    public TypesOfPositions convertToEntityAttribute(String dbData) throws IllegalArgumentException {
        if (dbData == null) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(TypesOfPositions.values())
                .filter(v -> v.getType().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
