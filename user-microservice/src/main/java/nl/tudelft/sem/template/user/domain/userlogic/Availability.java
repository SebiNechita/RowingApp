package nl.tudelft.sem.template.user.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "availabilities")
@NoArgsConstructor
//class to support users availabilities
public class Availability {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "start", nullable = false)
    LocalTime start;

    @Column(name = "start", nullable = false)
    LocalTime end;

    public Availability(int id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }
}
