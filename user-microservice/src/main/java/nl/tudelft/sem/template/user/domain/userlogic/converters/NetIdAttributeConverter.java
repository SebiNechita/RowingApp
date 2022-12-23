package nl.tudelft.sem.template.user.domain.userlogic.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;

/**
 * JPA Converter for the NetID value object.
 */
@Converter
public class NetIdAttributeConverter implements AttributeConverter<NetId, String> {

    @Override
    public String convertToDatabaseColumn(NetId attribute) {
        return attribute.toString();
    }

    @Override
    public NetId convertToEntityAttribute(String dbData) {
        return new NetId(dbData);
    }

}

