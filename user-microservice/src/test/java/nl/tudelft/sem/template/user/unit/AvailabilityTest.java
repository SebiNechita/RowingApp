package nl.tudelft.sem.template.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import nl.tudelft.sem.template.user.domain.userlogic.AmateurBuilder;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;
import nl.tudelft.sem.template.user.domain.userlogic.entities.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.AvailabilityOverlapException;
import org.junit.jupiter.api.Test;

class AvailabilityTest {

    @Test
    void generateAvailabilities() {
        Tuple<String, String> tupleOne = new Tuple<>("2022-12-12T13:30", "2022-12-12T15:00");
        Tuple<String, String> tupleTwo = new Tuple<>("2022-12-31T20:59", "2022-12-31T22:00");
        List<Tuple<String, String>> availabilities = List.of(tupleOne, tupleTwo);
        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T15:00");
        LocalDateTime dateOneIntervalTwo = LocalDateTime.parse("2022-12-31T20:59");
        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-31T22:00");
        TreeMap<LocalDateTime, LocalDateTime> treeMap = new TreeMap<>();
        treeMap.put(dateOneIntervalOne, dateTwoIntervalOne);
        treeMap.put(dateOneIntervalTwo, dateTwoIntervalTwo);
        try {
            assertEquals(treeMap, Availability.generateAvailabilities(availabilities));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateAvailabilitiesWhenStartIsEqual() {
        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-12T22:00");
        Tuple<String, String> tupleOne = new Tuple<>("2022-12-12T13:30", "2022-12-12T15:00");
        Tuple<String, String> tupleTwo = new Tuple<>("2022-12-12T13:30", "2022-12-12T22:00");

        List<Tuple<String, String>> availabilities = List.of(tupleOne, tupleTwo);
        assertThrows(AvailabilityOverlapException.class, () -> {
            Availability.generateAvailabilities(availabilities);
        });
    }

    @Test
    void overlap() {
        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T15:00");
        LocalDateTime dateOneIntervalTwo = LocalDateTime.parse("2022-12-12T13:40");
        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-12T22:00");
        TreeMap<LocalDateTime, LocalDateTime> treeMap = new TreeMap<>();
        treeMap.put(dateOneIntervalOne, dateTwoIntervalOne);
        treeMap.put(dateOneIntervalTwo, dateTwoIntervalTwo);
        assertTrue(Availability.overlap(treeMap));
    }


    @Test
    void notOverlap() {
        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T15:00");
        LocalDateTime dateOneIntervalTwo = LocalDateTime.parse("2022-12-31T13:40");
        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-31T22:00");
        TreeMap<LocalDateTime, LocalDateTime> treeMap = new TreeMap<>();
        treeMap.put(dateOneIntervalOne, dateTwoIntervalOne);
        treeMap.put(dateOneIntervalTwo, dateTwoIntervalTwo);
        assertFalse(Availability.overlap(treeMap));
    }

    @Test
    void test1() {
        AmateurBuilder builder = new AmateurBuilder();
        builder.setNetId(new NetId("netId"));
        builder.setPassword(new HashedPassword("123"));
        builder.setGender(Gender.FEMALE);
        System.out.println(builder.getUser().getClass().getSimpleName());
    }
}