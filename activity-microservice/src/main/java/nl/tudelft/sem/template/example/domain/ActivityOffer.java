package nl.tudelft.sem.template.example.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ActivityOffer {

    @Getter
    @NonNull
    private String position;

    @Getter
    private boolean isActive;

    @Getter
    @NonNull
    private LocalDateTime startTime;

    @Getter
    @NonNull
    private LocalDateTime endTime;

    @Getter
    @NonNull
    private String ownerId;

    @Getter
    private String boatCertificate;


}
