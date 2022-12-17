package nl.tudelft.sem.template.user.domain.userlogic.converters;

import nl.tudelft.sem.template.user.domain.userlogic.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class GenderConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender type) {
        return type.toString();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) throws IllegalArgumentException {
        if (dbData == null) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(Gender.values())
                .filter(v -> v.getGender().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}