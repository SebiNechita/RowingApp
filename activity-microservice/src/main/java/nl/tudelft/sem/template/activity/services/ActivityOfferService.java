package nl.tudelft.sem.template.activity.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.models.AvailableCompetitionsModel;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.GetUserDetailsModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<ActivityOffer> getFilteredOffers(NetId netId) throws Exception {
        try {

            //return activityOfferRepository.findAll().filterActivityBasedOnUserDetails();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8082/user/get/details/" + netId))
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
                throw new Exception();
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
    public boolean participantIsEligible(ParticipantIsEligibleRequestModel request) throws ResponseStatusException {
        Optional<ActivityOffer> activityOffer = activityOfferRepository.findById(request.getActivityOfferId());

        if (activityOffer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity with given ID not found");
        }

        // TODO(iannis): Retrieve certificates, gender, rank & organisation from user microservice.
        //               Return false if any of these don't match the activity offer requirements.

        return true;
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
                        .collect(Collectors.toList()));
    }

    /**
     * Send request to user microservice to get a user details.
     *
     * @param netId     netId
     * @param authToken authToken
     * @return UserDetailsModel
     */
    public UserDetailsModel getUserDetailsModel(NetId netId, String authToken) {
        String urlAddress = "http://localhost:8085/user/get/details";
        UserDetailsModel model = HttpUtils.sendAuthorizedHttpRequest(urlAddress, HttpMethod.GET,
                authToken, netId, UserDetailsModel.class).getBody();
        return model;
    }
}
