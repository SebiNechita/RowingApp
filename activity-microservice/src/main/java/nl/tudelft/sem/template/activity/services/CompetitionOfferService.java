package nl.tudelft.sem.template.activity.services;

import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.*;
import nl.tudelft.sem.template.common.models.user.Tuple;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CompetitionOfferService extends ActivityOfferService{

    public CompetitionOfferService(ActivityOfferRepository activityOfferRepository,
                                   DataValidation dataValidation,
                                   ActivityClient activityClient,
                                   UserModelParser userModelParser) {
        super(activityOfferRepository, dataValidation, activityClient, userModelParser);
    }

    /**
     * Creates a new CompetitionOffer and adds it to database.
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
     * @param organisation    organisation
     * @param isFemale        boolean is the competition for females
     * @param isPro           boolean is the competition for experienced rowers
     * @throws Exception EmptyStringException
     */
    public void createCompetitionOffer(TypesOfPositions position,
                                       boolean isActive,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime,
                                       String ownerId,
                                       String boatCertificate,
                                       TypesOfActivities type,
                                       String name,
                                       String description,
                                       String organisation,
                                       boolean isFemale,
                                       boolean isPro,
                                       String authToken) throws Exception {

        super.getDataValidation().validateData(startTime, endTime, name, description, boatCertificate, authToken);
        super.getDataValidation().validateOrganisation(organisation, authToken);

        CompetitionOffer competition = new CompetitionOffer(position, isActive, startTime, endTime,
                ownerId, boatCertificate, type, name, description, organisation, isFemale, isPro);

        super.getActivityOfferRepository().save(competition);
        System.out.println("Competition " + competition.toString() + " has been added to the database");
    }


    /**
     * Gets a list of ActivityOffers which are all active competitions.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getAllCompetitionOffers() throws Exception {
        try {
            return super.getActivityOfferRepository().findByType(TypesOfActivities.COMPETITION)
                    .stream()
                    .filter(ActivityOffer::isActive)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error while retrieving all competitions. " + e.getMessage());
        }
    }

    /**
     * Filters all the competitions such that they match the given requirements.
     *
     * @param organisation   organisation
     * @param isFemale       isFemale
     * @param isPro          isPro
     * @param certificates   certificates
     * @param positions      positions
     * @param availabilities availabilities
     * @return list of selected competitions
     * @throws Exception exception
     */
    public AvailableCompetitionsModel getFilteredCompetitionOffers(String organisation,
                                                                   boolean isFemale,
                                                                   boolean isPro,
                                                                   List<String> certificates,
                                                                   List<TypesOfPositions> positions,
                                                                   List<Tuple<LocalDateTime, LocalDateTime>> availabilities)
            throws Exception {
        return new AvailableCompetitionsModel(
                getAllCompetitionOffers()
                        .stream()
                        .map(x -> (CompetitionOffer) x)
                        .filter(x -> x.isFemale() == isFemale)
                        .filter(x -> x.isPro() == isPro)
                        .filter(x -> x.getOrganisation().equals(organisation))
                        .filter(x -> positions.contains(x.getPosition()))
                        .filter(x -> {
                            for (Tuple<LocalDateTime, LocalDateTime> tuple : availabilities) {
                                LocalDateTime avlStartTime = tuple.getFirst();
                                LocalDateTime avlEndTime = tuple.getSecond();

                                // If the competition doesn't start before our slot and doesn't end after our slot
                                // we can take part in it
                                if (!x.getStartTime().isBefore(avlStartTime) && !x.getEndTime().isAfter(avlEndTime)) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .filter(x -> {
                            if (x.getPosition().equals(TypesOfPositions.COX)) {
                                return certificates.contains(x.getBoatCertificate());
                            }
                            return true;
                        })
                        .map(x -> new CompetitionResponseModel(x.getId(), x.getPosition(), x.isActive(),
                                x.getStartTime(), x.getEndTime(), x.getOwnerId(), x.getBoatCertificate(), x.getType(),
                                x.getName(), x.getDescription(), x.getOrganisation(), x.isFemale(), x.isPro()))
                        .collect(Collectors.toList()));

    }

    public boolean participantIsEligible(ParticipantIsEligibleRequestModel request, String authToken)
            throws ResponseStatusException {
        return super.participantIsEligible(request, authToken);
    }
}
