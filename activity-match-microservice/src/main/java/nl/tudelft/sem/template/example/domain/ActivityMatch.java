package nl.tudelft.sem.template.example.domain;

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
import nl.tudelft.sem.template.common.models.activity_match.TypesOfActivities;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activities")
@ToString
public class ActivityMatch {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Getter
    @NonNull
    @Column(name = "userId", nullable = false)
    private String userId;

    @Getter
    @NonNull
    @Column(name = "activityId", nullable = false)
    private String activityId;
    @Getter
    @NonNull
    @Column(name = "ownerName", nullable = false)
    private String ownerId;


    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Convert(converter = TypeOfActivityConverter.class)
    private TypesOfActivities type;

    /**
     * Initialises an ActivityOffer without an Id.
     *
     * @param userId          userId
     * @param activityId      activityId
     * @param ownerId         ownerId
     * @param type            type
     */
    public ActivityMatch(@NonNull String ownerId,
                         @NonNull String activityId,
                         @NonNull String userId,
                         TypesOfActivities type) {
        this.ownerId = ownerId;
        this.userId = userId;
        this.activityId = activityId;
        this.type = type;
    }
}
