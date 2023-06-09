package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
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
import lombok.Setter;
import lombok.ToString;
import nl.tudelft.sem.template.common.models.activity.TypeOfActivityConverter;
import nl.tudelft.sem.template.common.models.activity.TypeOfPositionConverter;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

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
    @Setter
    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "position", nullable = false)
    @Convert(converter = TypeOfPositionConverter.class)
    private TypesOfPositions position;

    @Getter
    @Setter
    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    @Getter
    @Setter
    @NonNull
    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Getter
    @Setter
    @NonNull
    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @Getter
    @Setter
    @NonNull
    @Column(name = "ownerName", nullable = false)
    private String ownerId;

    @Getter
    @Setter
    @Column(name = "certificates", nullable = false)
    private String boatCertificate;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Convert(converter = TypeOfActivityConverter.class)
    private TypesOfActivities type;

    @Getter
    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @NonNull
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Initialises an ActivityOffer without an Id, name and description.
     *
     * @param position        position
     * @param isActive        isActive
     * @param startTime       startTime
     * @param endTime         endTime
     * @param ownerId         ownerId
     * @param boatCertificate boatCertificate
     * @param type            type
     */
    public ActivityOffer(@NonNull TypesOfPositions position,
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
     * @param name            name
     * @param description     description
     */
    public ActivityOffer(@NonNull TypesOfPositions position,
                         boolean isActive,
                         @NonNull LocalDateTime startTime,
                         @NonNull LocalDateTime endTime,
                         @NonNull String ownerId,
                         String boatCertificate,
                         TypesOfActivities type,
                         @NonNull String name,
                         @NonNull String description) {
        this.position = position;
        this.isActive = isActive;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerId = ownerId;
        this.boatCertificate = boatCertificate;
        this.type = type;
        this.name = name;
        this.description = description;
    }
}
