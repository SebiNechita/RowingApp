package nl.tudelft.sem.template.activity.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.user.GetUserDetailsModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivityOfferService {

    private final transient ActivityOfferRepository activityOfferRepository;
    private final transient UserMicroserviceAdapter userMicroserviceAdapter;
    static final Logger logger = LoggerFactory.getLogger(ActivityOfferService.class.getName());

    /**
     * Instantiates a new ActivityOfferService.
     *
     * @param activityOfferRepository activityOfferRepository
     */
    public ActivityOfferService(ActivityOfferRepository activityOfferRepository,
                                UserMicroserviceAdapter userMicroserviceAdapter) {
        this.activityOfferRepository = activityOfferRepository;
        this.userMicroserviceAdapter = userMicroserviceAdapter;
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
                                    String description) throws Exception {
        if (!startTime.isBefore(endTime)) {
            throw new NotCorrectIntervalException("Start time of the interval has to be before the end time.");
        }
        if (name.isEmpty()) {
            throw new EmptyStringException("Name");
        }
        if (description.isEmpty()) {
            throw new EmptyStringException("Description");
        }

        TrainingOffer training = new TrainingOffer(position, isActive, startTime, endTime,
                ownerId, boatCertificate, type, name, description);

        activityOfferRepository.save(training);
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
                                         String description) throws Exception {
        try {

            for (TypesOfPositions position : positions.keySet()) {
                int amountToCreate = positions.get(position);
                for (int i = 0; i < amountToCreate; i++) {
                    TrainingOffer training = setTrainingParameters(position, isActive,
                            startTime, endTime,
                            ownerId, boatCertificate,
                            type, name, description);
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
     * Gets the list with all the ActivityOffer.
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
     * Gets a filtered list of ActivityOffer.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getFilteredOffers(NetId netId) throws Exception{
        try {

            //return activityOfferRepository.findAll().filterActivityBasedOnUserDetails();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8082/user/get/details/"+ netId))
                    .build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

// Check if the request was successful
            if (response.statusCode() == HttpStatus.OK.value()) {
                // Parse the response body
                ObjectMapper mapper = new ObjectMapper();
                GetUserDetailsModel model = mapper.readValue(response.body(), GetUserDetailsModel.class);
                List<String> availabilities = model.getAvailabilities();
                System.out.println(availabilities);
                // Use the data in the model object as needed
                // ...
            } else {
                // The request was not successful. Handle the error as appropriate.
                // ...
            }
            return activityOfferRepository.findAll();
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating ActivityOffer. " + e.getMessage());
        }
    }


    /**
     * Endpoint for checking if a participant is eligible to join a given activity.
     *
     * @param request wrapped in a ParticipantIsEligibleRequestModel.
     * @return boolean indicating eligibility.
     * @throws ResponseStatusException if not successful.
     */
    public boolean participantIsEligible(ParticipantIsEligibleRequestModel request, String authToken)
            throws ResponseStatusException {
        logger.info(String.format("received participantIsEligible request for activity %d, user %s",
                request.getActivityOfferId(), request.getParticipantNetid()));

        Optional<ActivityOffer> activityOffer = activityOfferRepository.findById(request.getActivityOfferId());

        if (activityOffer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity with given ID not found");
        }

        NetId participantNetId = new NetId(request.getParticipantNetid());

        GetUserDetailsModel userDetails = userMicroserviceAdapter.getUserDetails(participantNetId, authToken).getBody();

        if (activityOffer.get().getBoatCertificate() != null
                && !userDetails.getCertificates().contains(activityOffer.get().getBoatCertificate())) {
            logger.info("user is ineligible because they don't have the required certificate %s",
                    activityOffer.get().getBoatCertificate());
            return false;
        }

        // TODO(iannis): Also check gender, rank & organisation.
        //               Currently this is not possible, since this data does not exist on the ActivityOffer.
        //               Return false if any of these don't match the activity offer requirements.

        return true;
    }
}
