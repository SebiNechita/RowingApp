package nl.tudelft.sem.template.user.domain.userlogic.converters;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;


@Converter
public class TypeOfPositionConverter implements AttributeConverter<TypesOfPositions, String> {

    @Override
    public String convertToDatabaseColumn(TypesOfPositions position) {
        return position.getType();
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
