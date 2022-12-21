package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

public interface ActivityOfferBuilder<T extends ActivityOffer> {

    ActivityOfferBuilder<T> setPosition(TypesOfPositions position);

    ActivityOfferBuilder<T> setActive(boolean isActive);

    ActivityOfferBuilder<T> setStartTime(LocalDateTime startTime);

    ActivityOfferBuilder<T> setEndTime(LocalDateTime endTime);

    ActivityOfferBuilder<T> setOwnerId(String ownerId);

    ActivityOfferBuilder<T> setBoatCertificate(String boatCertificate);

    ActivityOfferBuilder<T> setType(TypesOfActivities type);

    ActivityOfferBuilder<T> setName(String name);

    ActivityOfferBuilder<T> setDescription(String description);

    T build();
}
