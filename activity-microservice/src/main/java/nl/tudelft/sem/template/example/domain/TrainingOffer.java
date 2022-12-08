package nl.tudelft.sem.template.example.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@NoArgsConstructor
public class TrainingOffer extends ActivityOffer {

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
     */
    public TrainingOffer(@NonNull String position,
                         boolean isActive,
                         @NonNull LocalDateTime startTime,
                         @NonNull LocalDateTime endTime,
                         @NonNull String ownerId,
                         String boatCertificate,
                         //TypesOfActivities type
                         String type) {
        super(position, isActive, startTime, endTime, ownerId, boatCertificate, type);
    }
}
