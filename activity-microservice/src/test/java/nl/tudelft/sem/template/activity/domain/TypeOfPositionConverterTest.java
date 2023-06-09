package nl.tudelft.sem.template.activity.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.tudelft.sem.template.common.models.activity.TypeOfPositionConverter;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TypeOfPositionConverterTest {

    private TypesOfPositions type;
    private TypeOfPositionConverter converter;

    @BeforeEach
    void setup() {
        this.converter = new TypeOfPositionConverter();
        this.type = TypesOfPositions.COX;
    }

    @Test
    void convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(type)).isEqualTo("COX");
    }

    @Test
    void convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute("cox")).isEqualTo(type);
    }

    @Test
    void convertToEntityAttribute_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute(null);
        });
    }

    @Test
    void convertToEntityAttribute_starboardRower() {
        this.type = TypesOfPositions.STARBOARD_ROWER;
        assertThat(converter.convertToEntityAttribute("starboard_rower")).isEqualTo(type);
    }

}
