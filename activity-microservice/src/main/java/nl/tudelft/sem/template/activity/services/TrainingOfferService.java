package nl.tudelft.sem.template.activity.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.NetId;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrainingOfferService extends ActivityOfferService{

    public TrainingOfferService(ActivityOfferRepository activityOfferRepository,
                                DataValidation dataValidation,
                                ActivityClient activityClient,
                                UserModelParser userModelParser) {
        super(activityOfferRepository, dataValidation, activityClient, userModelParser);
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

    /**
     * Gets the list with all the ActivityOffer.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getAllTrainingOffers() throws Exception {
        try {
            return super.getActivityOfferRepository().findAll();
        } catch (Exception e) {
            System.out.println("Exception in the service for getting all training offers");
            throw new Exception("Error while creating getting all the TrainingOffer. " + e.getMessage());
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
     * Gets a filtered list of ActivityOffer.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getFilteredTrainings(NetId netId, String authToken) throws Exception {
        try {
            HttpResponse<String> response = super.getActivityClient().getUserDetails("http://localhost:8082/user/get/details/" + netId, authToken);

            // Check if the request was successful
            if (response.statusCode() == HttpStatus.OK.value()) {
                UserDetailsModel model = super.getUserModelParser().getModel(response);
                return filterOffers(model);
            } else {
                return List.of();
            }
        } catch (Exception e) {
            System.out.println("Exception in the service for getting filtered training offers");
            throw new Exception("Error while filtering the training offers. " + e.getMessage());
        }
    }

    /**
     * Parse the retrieved response and filter offers that match the user detials.
     *
     * @param model user details
     */
    private List<ActivityOffer> filterOffers(UserDetailsModel model) {
        List<Tuple<LocalDateTime, LocalDateTime>> availabilities = model.getAvailabilities();

        List<ActivityOffer> offers = super.getActivityOfferRepository().findAll();
        List<ActivityOffer> filteredOffers = new ArrayList<>();

        for (ActivityOffer offer : offers) {
            for (Tuple<LocalDateTime, LocalDateTime> availability : availabilities) {
                if (!offer.getStartTime().isBefore(availability.getFirst())
                        && !offer.getEndTime().isAfter(availability.getSecond())) {
                    filteredOffers.add(offer);
                }
            }
        }
        System.out.println(availabilities);
        return filteredOffers;
    }

    public boolean participantIsEligible(ParticipantIsEligibleRequestModel request, String authToken)
            throws ResponseStatusException {
        return super.participantIsEligible(request, authToken);
    }
}
