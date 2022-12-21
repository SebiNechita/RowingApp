package nl.tudelft.sem.template.user.domain.userlogic;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Converter
public class TypeOfPositionConverter implements AttributeConverter<List<TypesOfPositions>, String> {

    @Override
    public String convertToDatabaseColumn(List<TypesOfPositions> type) {
        return type.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<TypesOfPositions> convertToEntityAttribute(String dbData) throws IllegalArgumentException {
        if (dbData == null) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .map(TypesOfPositions::valueOf)
                .collect(Collectors.toList());
    }
}
