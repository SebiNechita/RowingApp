package nl.tudelft.sem.template.user.unit;

import nl.tudelft.sem.template.user.domain.userlogic.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

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
    void overlap() {
    }
}