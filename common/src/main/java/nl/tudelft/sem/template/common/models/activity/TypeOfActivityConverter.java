package nl.tudelft.sem.template.common.models.activity;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TypeOfActivityConverter implements AttributeConverter<TypesOfActivities, String> {

    @Override
    public String convertToDatabaseColumn(TypesOfActivities type) {
        return type.toString();
    }

    @Override
    public TypesOfActivities convertToEntityAttribute(String dbData) throws IllegalArgumentException {
        if (dbData == null) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(TypesOfActivities.values())
                .filter(v -> v.getType().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
