package nl.tudelft.sem.template.activity.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
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

}
