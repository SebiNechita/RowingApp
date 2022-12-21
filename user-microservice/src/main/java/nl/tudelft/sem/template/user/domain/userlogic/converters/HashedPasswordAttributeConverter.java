package nl.tudelft.sem.template.user.domain.userlogic.converters;

import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for the HashedPassword value object.
 */
@Converter
public class HashedPasswordAttributeConverter implements AttributeConverter<HashedPassword, String> {

    @Override
    public String convertToDatabaseColumn(HashedPassword attribute) {
        return attribute.toString();
    }

    @Override
    public HashedPassword convertToEntityAttribute(String dbData) {
        return new HashedPassword(dbData);
    }

}

