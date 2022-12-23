package nl.tudelft.sem.template.user.domain.userlogic.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.converters.NetIdAttributeConverter;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.AvailabilityOverlapException;

@Entity
@Table(name = "availabilities")
@Getter
@NoArgsConstructor
//class to support users availabilities
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false)
    @Convert(converter = NetIdAttributeConverter.class)
    NetId netId;

    @Column(name = "start", nullable = false)
    LocalDateTime start;

    @Column(name = "end", nullable = false)
    LocalDateTime end;

    /**
     * Create new user availability.
     *
     * @param netId The NetId for the new user
     * @param start The start of the interval when a user is available for training/competing
     * @param end The start of the interval when a user is available for training/competing
     */
    public Availability(@NonNull NetId netId,
                        @NonNull LocalDateTime start,
                        @NonNull LocalDateTime end) {
        this.netId = netId;
        this.start = start;
        this.end = end;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Availability that = (Availability) o;
        return id == that.id;
    }

    /**
     * Method that compare the attributes of two availabilities but not the id.
     */
    /*
    public boolean equalAttributes(Object o) {
        if (o.equals(this)) {
            return true;
        }
        Availability that = (Availability) o;
        return this.netId.equals(that.netId) && this.start.equals(that.start) && this.end.equals(that.end);
    }
    */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.start);
        result.append(",");
        result.append(this.end);
        return result.toString();
    }

    /**
     * Parse a list of string tuples representing user's availabilities
     * into a TreeMap of LocalDateTimes.
     * I chose a TreeMap as it is an efficient way to store intervals
     *
     * @param availabilitiesAsStrings the List of  user's availabilities, as a List of String Tuples
     */
    public static TreeMap<LocalDateTime, LocalDateTime> generateAvailabilities(
            List<Tuple<String, String>> availabilitiesAsStrings)
            throws Exception {
        TreeMap<LocalDateTime, LocalDateTime> availabilities = new TreeMap<>();
        for (Tuple<String, String> currentStringTuple : availabilitiesAsStrings) {
            LocalDateTime start = LocalDateTime.parse(
                    currentStringTuple.getFirst(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            if (!availabilities.containsKey(start)) {
                LocalDateTime end = LocalDateTime.parse(
                        currentStringTuple.getSecond(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                availabilities.put(start, end);
            } else {
                throw new AvailabilityOverlapException();
            }
        }
        return availabilities;
    }

    /**
     * This method iterates over the three and checks weather there are any overlaps
     * in the intervals given in the TreeMap or not.
     *
     * @param intervals the List of  user's availabilities as a TreeMap
     */
    public static boolean overlap(TreeMap<LocalDateTime, LocalDateTime> intervals) {
        List<Map.Entry<LocalDateTime, LocalDateTime>> availabilities = new ArrayList<>(intervals.entrySet());
        for (int i = 1; i < availabilities.size(); i++) {
            if (availabilities.get(i).getKey().isBefore(availabilities.get(i - 1).getValue())) {
                return true;
            }
        }
        return false;
    }

}

