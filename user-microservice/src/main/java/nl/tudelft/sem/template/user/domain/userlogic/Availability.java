package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    LocalTime start;

    @Column(name = "end", nullable = false)
    LocalTime end;

    public Availability(NetId netId, LocalTime start, LocalTime end) {
        this.netId = netId;
        this.start = start;
        this.end = end;
    }

    public static TreeMap<LocalTime, LocalTime> generateAvailabilities(List<Tuple<String, String>> availabilitiesAsStrings) throws Exception{
        TreeMap<LocalTime, LocalTime> availabilities = new TreeMap<>();
        String pattern = "HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            for (Tuple<String, String> currentStringTuple : availabilitiesAsStrings) {
                LocalTime start = LocalTime.parse(currentStringTuple.getFirst(), formatter);
                LocalTime end = LocalTime.parse(currentStringTuple.getSecond(), formatter);
                availabilities.put(start, end);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            // Display or log the error message
            System.out.println("An error occurred: " + errorMessage);
        }
        return availabilities;
    }
}
