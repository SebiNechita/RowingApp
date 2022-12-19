package nl.tudelft.sem.template.user.domain.userlogic.converters;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;

@Converter
public class GenderConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender type) {
        return type.getGender();
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