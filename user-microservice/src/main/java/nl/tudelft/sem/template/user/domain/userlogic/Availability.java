package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "availabilities")
@NoArgsConstructor
//class to support users availabilities
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    NetId netId;

    @Column(name = "start", nullable = false)
    LocalDateTime start;

    @Column(name = "end", nullable = false)
    LocalDateTime end;

    public Availability(NetId netId, LocalDateTime start, LocalDateTime end) {
        this.netId = netId;
        this.start = start;
        this.end = end;
    }

    public static TreeMap<LocalDateTime, LocalDateTime> generateAvailabilities(List<Tuple<String, String>> availabilitiesAsStrings) throws Exception{
        TreeMap<LocalDateTime, LocalDateTime> availabilities = new TreeMap<>();
        String pattern = "yyyy-MM-dd'T'HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            for (Tuple<String, String> currentStringTuple : availabilitiesAsStrings) {
                LocalDateTime start = LocalDateTime.parse(currentStringTuple.getFirst(), formatter);
                LocalDateTime end = LocalDateTime.parse(currentStringTuple.getSecond(), formatter);
                availabilities.put(start, end);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            // Display or log the error message
            System.out.println("An error occurred: " + errorMessage);
        }
        return availabilities;
    }

    public static boolean overlap(TreeMap<LocalDateTime, LocalDateTime> availabilities) {
        for (Map.Entry<LocalDateTime, LocalDateTime> interval : availabilities.entrySet()) {
            LocalDateTime start = interval.getKey();
            for (Map.Entry<LocalDateTime, LocalDateTime> previous : availabilities.headMap(start).entrySet()) {
                LocalDateTime previousEnd = previous.getValue();
                if (previousEnd.isAfter(start)) return true;
            }
        }
        return false;
    }
}
