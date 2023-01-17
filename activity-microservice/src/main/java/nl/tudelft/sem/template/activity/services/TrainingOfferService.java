package nl.tudelft.sem.template.activity.services;

import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

import java.time.LocalDateTime;
import java.util.Map;

public class TrainingOfferService extends ActivityOfferService{

    public TrainingOfferService(ActivityOfferRepository activityOfferRepository,
                                DataValidation dataValidation) {
        super(activityOfferRepository, dataValidation);
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


        super.getDataValidation().validateData(startTime, endTime, name, description, boatCertificate, authToken);
        TrainingOffer training = new TrainingOffer(position, isActive, startTime, endTime,
                ownerId, boatCertificate, type, name, description);

        super.getActivityOfferRepository().save(training);
        System.out.println("Training " + training.toString() + " has been added to the database");
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
                    super.getDataValidation().validateData(startTime, endTime, name, description, boatCertificate, authToken);
                    super.getActivityOfferRepository().save(training);
                    System.out.println("Training " + training + " has been added to the database");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in the service for creating multiple training offers");
            throw new Exception("Error while creating many ActivityOffer. " + e.getMessage());
        }
    }
}
