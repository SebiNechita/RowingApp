package nl.tudelft.sem.template.activity.services;

import java.time.LocalDateTime;
import java.util.Map;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.TypesOfActivities;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.models.ManyTrainingsCreationRequestModel;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityOfferService {

    private final transient ActivityOfferRepository activityOfferRepository;

    /**
     * Instantiates a new ActivityOfferService.
     *
     * @param activityOfferRepository activityOfferRepository
     */
    public ActivityOfferService(ActivityOfferRepository activityOfferRepository) {
        this.activityOfferRepository = activityOfferRepository;
    }

    /**
     * Creates a new TrainingOffer and adds it to database.
     *
     * @param request TrainingOffer model sent by user
     * @throws Exception exception
     */
    public void createTrainingOffer(TrainingCreationRequestModel request) throws Exception {
        try {
            TrainingOffer training = setTrainingParameters(request.getPosition(), request.isActive(),
                                                            request.getStartTime(), request.getEndTime(),
                                                            request.getOwnerId(), request.getBoatCertificate(),
                                                            request.getType());
            activityOfferRepository.save(training);
            System.out.println("Training " + training.toString() + " has been added to the database");
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating ActivityOffer. " + e.getMessage());
        }
    }

    /**
     * Creates a multiple TrainingOffers and adds them to database.
     *
     * @param request TrainingOffer model sent by user
     * @throws Exception exception
     */
    public void createManyTrainingOffers(ManyTrainingsCreationRequestModel request) throws Exception {
        try {
            Map<TypesOfPositions, Integer> map = request.getPositions();

            for (TypesOfPositions position : map.keySet()) {
                int amountToCreate = map.get(position);
                for (int i = 0; i < amountToCreate; i++) {
                    TrainingOffer training = setTrainingParameters(position, request.isActive(),
                                                                    request.getStartTime(), request.getEndTime(),
                                                                    request.getOwnerId(), request.getBoatCertificate(),
                                                                    request.getType());
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
     * @param position postition
     * @param isActive isActive
     * @param startTime startTime
     * @param endTime endTime
     * @param ownerId ownerId
     * @param boatCertificate boatCertificate
     * @param type type
     * @return created training
     */
    public TrainingOffer setTrainingParameters(TypesOfPositions position, boolean isActive,
                                               LocalDateTime startTime, LocalDateTime endTime,
                                               String ownerId, String boatCertificate,
                                               TypesOfActivities type) {
        TrainingOfferBuilder builder = new TrainingOfferBuilder();
        builder.setPosition(position)
                .setActive(isActive)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setOwnerId(ownerId)
                .setBoatCertificate(boatCertificate)
                .setType(type);
        return builder.build();
    }
}
