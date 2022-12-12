package nl.tudelft.sem.template.activity.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeOfActivityConverterTest {


    private TypesOfActivities type;
    private TypeOfActivityConverter converter;

    @BeforeEach
    void setup() {
        this.converter = new TypeOfActivityConverter();
        this.type = TypesOfActivities.TRAINING;
    }

    @Test
    void convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(type)).isEqualTo("TRAINING");
    }

    @Test
    void convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute("training")).isEqualTo(type);
    }

    @Test
    void convertToEntityAttribute_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute(null);
        });
    }
}