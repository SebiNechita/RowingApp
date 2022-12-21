package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

public class CompetitionOfferBuilder implements ActivityOfferBuilder<CompetitionOffer> {

    private transient TypesOfPositions position;
    private transient boolean isActive;
    private transient LocalDateTime startTime;
    private transient LocalDateTime endTime;
    private transient String ownerId;
    private transient String boatCertificate;
    private transient TypesOfActivities type = TypesOfActivities.COMPETITION;
    private transient String name;
    private transient String description;
    private transient String organisation;
    private transient boolean isFemale;
    private transient boolean isPro;

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setPosition(TypesOfPositions position) {
        this.position = position;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setBoatCertificate(String boatCertificate) {
        this.boatCertificate = boatCertificate;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setType(TypesOfActivities type) {
        this.type = type;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setOrganisation(String organisation) {
        this.organisation = organisation;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setFemale(boolean isFemale) {
        this.isFemale = isFemale;
        return this;
    }

    @Override
    public ActivityOfferBuilder<CompetitionOffer> setPro(boolean isPro) {
        this.isPro = isPro;
        return this;
    }

    @Override
    public CompetitionOffer build() {
        return new CompetitionOffer(position, isActive,
                startTime, endTime,
                ownerId, boatCertificate,
                type, name, description,
                organisation, isFemale, isPro);
    }
}
