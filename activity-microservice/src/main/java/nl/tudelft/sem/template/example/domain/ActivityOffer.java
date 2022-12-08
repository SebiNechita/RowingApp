package nl.tudelft.sem.template.example.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
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


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offers")
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
    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Getter
    @NonNull
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
    private TypesOfActivities type;

}
