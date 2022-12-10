package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offers")
@ToString
public abstract class ActivityOffer {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Getter
    @NonNull
    @Column(name = "position", nullable = false)
    private String position;

    @Getter
    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    @Getter
    @NonNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Getter
    @NonNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @Getter
    @NonNull
    @Column(name = "ownerName", nullable = false)
    private String ownerId;

    @Getter
    @Column(name = "certificates", nullable = false)
    private String boatCertificate;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Convert(converter = TypeOfActivityConverter.class)
    private TypesOfActivities type;

    /**
     * Initialises an ActivityOffer without an Id.
     *
     * @param position        position
     * @param isActive        isActive
     * @param startTime       startTime
     * @param endTime         endTime
     * @param ownerId         ownerId
     * @param boatCertificate boatCertificate
     * @param type            type
     */
    public ActivityOffer(@NonNull String position,
                         boolean isActive,
                         @NonNull LocalDateTime startTime,
                         @NonNull LocalDateTime endTime,
                         @NonNull String ownerId,
                         String boatCertificate,
                         TypesOfActivities type) {
        this.position = position;
        this.isActive = isActive;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerId = ownerId;
        this.boatCertificate = boatCertificate;
        this.type = type;
    }
}
