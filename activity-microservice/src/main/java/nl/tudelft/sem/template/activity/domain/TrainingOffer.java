package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Entity
public class TrainingOffer extends ActivityOffer {

    /**
     * Initialises a new TrainingOffer without an Id, name and description.
     *
     * @param position        position
     * @param isActive        isActive
     * @param startTime       startTime
     * @param endTime         endTime
     * @param ownerId         ownerId
     * @param boatCertificate boatCertificate
     * @param type            type
     */
    public TrainingOffer(@NonNull String position,
                         boolean isActive,
                         @NonNull LocalDateTime startTime,
                         @NonNull LocalDateTime endTime,
                         @NonNull String ownerId,
                         String boatCertificate,
                         TypesOfActivities type) {
        super(position, isActive, startTime, endTime, ownerId, boatCertificate, type);
    }

    /**
     * Initialises a new TrainingOffer without an Id.
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
    public TrainingOffer(@NonNull String position,
                         boolean isActive,
                         @NonNull LocalDateTime startTime,
                         @NonNull LocalDateTime endTime,
                         @NonNull String ownerId,
                         String boatCertificate,
                         TypesOfActivities type,
                         String name,
                         String description) {
        super(position, isActive, startTime, endTime, ownerId, boatCertificate, type, name, description);
    }

    public String toString() {
        return super.toString();
    }
}
