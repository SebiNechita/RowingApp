package nl.tudelft.sem.template.activity.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.Tuple;
import org.springframework.stereotype.Service;

@Service
public class ActivityOfferService {

    private final transient ActivityOfferRepository activityOfferRepository;
    private final transient DataValidation dataValidation;


    /**
     * Instantiates a new ActivityOfferService.
     *
     * @param activityOfferRepository activityOfferRepository
     */
    public ActivityOfferService(ActivityOfferRepository activityOfferRepository,
                                DataValidation dataValidation) {
        this.activityOfferRepository = activityOfferRepository;
        this.dataValidation = dataValidation;
    }

    /**
     * Creates a new TrainingOffer and adds it to database.
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
     * @throws Exception EmptyStringException
     */
    public void createTrainingOffer(TypesOfPositions position,
                                    boolean isActive,
                                    LocalDateTime startTime,
                                    LocalDateTime endTime,
                                    String ownerId,
                                    String boatCertificate,
                                    TypesOfActivities type,
                                    String name,
                                    String description,
                                    String authToken) throws Exception {

        try {
            dataValidation.validateData(startTime, endTime, name, description, boatCertificate, authToken);
        } catch (NotCorrectIntervalException interEx) {
            throw new NotCorrectIntervalException(interEx.getMessage());
        } catch (EmptyStringException strEx) {
            throw new EmptyStringException(strEx.getMessage());
        } catch (InvalidCertificateException certEx) {
            throw new InvalidCertificateException(boatCertificate);
        }

        TrainingOffer training = new TrainingOffer(position, isActive, startTime, endTime,
                ownerId, boatCertificate, type, name, description);

        activityOfferRepository.save(training);
        System.out.println("Training " + training.toString() + " has been added to the database");
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

        dataValidation.validateData(startTime, endTime, name, description, boatCertificate, authToken);
        dataValidation.validateOrganisation(organisation, authToken);

        CompetitionOffer competition = new CompetitionOffer(position, isActive, startTime, endTime,
                ownerId, boatCertificate, type, name, description, organisation, isFemale, isPro);

        activityOfferRepository.save(competition);
        System.out.println("Competition " + competition.toString() + " has been added to the database");
    }

    /**
     * Creates a multiple TrainingOffers and adds them to database.
     *
     * @param positions       positions
     * @param isActive        isActive
     * @param startTime       startTime
     * @param endTime         endTime
     * @param ownerId         ownerId
     * @param boatCertificate boatCertificate
     * @param type            type
     * @param name            name
     * @param description     description
     * @throws Exception exception
     */
    public void createManyTrainingOffers(Map<TypesOfPositions, Integer> positions,
                                         boolean isActive,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime,
                                         String ownerId,
                                         String boatCertificate,
                                         TypesOfActivities type,
                                         String name,
                                         String description,
                                         String authToken) throws Exception {
        try {

            for (TypesOfPositions position : positions.keySet()) {
                int amountToCreate = positions.get(position);
                for (int i = 0; i < amountToCreate; i++) {
                    TrainingOffer training = setTrainingParameters(position, isActive,
                            startTime, endTime,
                            ownerId, boatCertificate,
                            type, name, description);
                    dataValidation.validateData(startTime, endTime, name, description, boatCertificate, authToken);
                    activityOfferRepository.save(training);
                    System.out.println("Training " + training + " has been added to the database");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating ActivityOffer. " + e.getMessage());
        }
    }

    /**
     * Creates a training from given parameters.
     *
     * @param position        postition
     * @param isActive        isActive
     * @param startTime       startTime
     * @param endTime         endTime
     * @param ownerId         ownerId
     * @param boatCertificate boatCertificate
     * @param type            type
     * @param name            name
     * @param description     description
     */
    public TrainingOffer setTrainingParameters(TypesOfPositions position, boolean isActive,
                                               LocalDateTime startTime, LocalDateTime endTime,
                                               String ownerId, String boatCertificate,
                                               TypesOfActivities type, String name, String description) {
        TrainingOfferBuilder builder = new TrainingOfferBuilder();
        builder.setPosition(position)
                .setActive(isActive)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setOwnerId(ownerId)
                .setBoatCertificate(boatCertificate)
                .setType(type)
                .setName(name)
                .setDescription(description);
        return builder.build();
    }


    /**
     * Gets a list of ActivityOffer.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getAllTrainingOffers() throws Exception {
        try {
            return activityOfferRepository.findAll();
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating ActivityOffer. " + e.getMessage());
        }
    }

    /**
     * Gets a list of ActivityOffers which are all active competitions.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getAllCompetitionOffers() throws Exception {
        try {
            return activityOfferRepository.findByType(TypesOfActivities.COMPETITION)
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
    public List<CompetitionOffer> getFilteredCompetitionOffers(String organisation,
                                                            boolean isFemale,
                                                            boolean isPro,
                                                            List<String> certificates,
                                                            List<TypesOfPositions> positions,
                                                            List<Tuple<LocalDateTime, LocalDateTime>> availabilities)
            throws Exception {
        return getAllCompetitionOffers()
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
                .collect(Collectors.toList());
    }
}
