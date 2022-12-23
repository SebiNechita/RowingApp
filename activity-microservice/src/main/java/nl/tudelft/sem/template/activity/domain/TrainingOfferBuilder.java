package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

public class TrainingOfferBuilder implements ActivityOfferBuilder<TrainingOffer> {

    private transient TypesOfPositions position;
    private transient boolean isActive;
    private transient LocalDateTime startTime;
    private transient LocalDateTime endTime;
    private transient String ownerId;
    private transient String boatCertificate;
    private transient TypesOfActivities type;
    private transient String name;
    private transient String description;

    @Override
    public ActivityOfferBuilder<TrainingOffer> setPosition(TypesOfPositions position) {
        this.position = position;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setBoatCertificate(String boatCertificate) {
        this.boatCertificate = boatCertificate;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setType(TypesOfActivities type) {
        this.type = type;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setOrganisation(String organisation) {
        return null;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setFemale(boolean isFemale) {
        return null;
    }

    @Override
    public ActivityOfferBuilder<TrainingOffer> setPro(boolean isPro) {
        return null;
    }

    @Override
    public TrainingOffer build() {
        return new TrainingOffer(position, isActive,
                startTime, endTime,
                ownerId, boatCertificate,
                type, name, description);
    }
}
