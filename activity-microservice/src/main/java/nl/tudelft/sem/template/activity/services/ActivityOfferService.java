package nl.tudelft.sem.template.activity.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.AvailableCompetitionsModel;
import nl.tudelft.sem.template.common.models.activity.CompetitionResponseModel;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositionsDeserializer;
import nl.tudelft.sem.template.common.models.user.NetId;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.TupleDeserializer;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public abstract class ActivityOfferService {

    private final transient ActivityOfferRepository activityOfferRepository;
    private final transient DataValidation dataValidation;


    /**
     * Instantiates a new ActivityOfferService.
     *
     * @param activityOfferRepository activityOfferRepository
     * @param dataValidation          dataValidation
     */
    public ActivityOfferService(ActivityOfferRepository activityOfferRepository,
                                DataValidation dataValidation) {
        this.activityOfferRepository = activityOfferRepository;
        this.dataValidation = dataValidation;
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
            System.out.println("Exception in the service for getting all training offers");
            throw new Exception("Error while creating getting all the TrainingOffer. " + e.getMessage());
        }
    }

    /**
     * Gets a filtered list of ActivityOffer.
     *
     * @throws Exception exception
     */
    public List<ActivityOffer> getFilteredTrainings(NetId netId, String authToken) throws Exception {
        try {
            HttpResponse<String> response = getUserDetails("http://localhost:8082/user/get/details/" + netId, authToken);

            // Check if the request was successful
            if (response.statusCode() == HttpStatus.OK.value()) {
                return parseResponseAndFilter(response);
            } else {
                return List.of();
            }
        } catch (Exception e) {
            System.out.println("Exception in the service for getting filtered training offers");
            throw new Exception("Error while filtering the training offers. " + e.getMessage());
        }
    }

    /**
     * Send request to the User microservice to get the user details.
     *
     * @param netId     netId
     * @param authToken authToken
     * @return HttpResponse         HttpResponse
     * @throws IOException          exception
     * @throws InterruptedException exceptions
     */
    private static HttpResponse<String> getUserDetails(String netId, String authToken)
            throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(netId))
                .header("Authorization", authToken)
                .build();
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * Parse the retrieved response and filter offers that match the user detials.
     *
     * @param response user details
     * @return list of ActivityOffers (Trainings)
     * @throws JsonProcessingException exception
     */
    private List<ActivityOffer> parseResponseAndFilter(HttpResponse<String> response) throws JsonProcessingException {
        // Parse the response body
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(TypesOfPositions.class, new TypesOfPositionsDeserializer());
        module.addDeserializer(Tuple.class, new TupleDeserializer());
        mapper.registerModule(module);

        UserDetailsModel model = mapper.readValue(response.body(), UserDetailsModel.class);
        List<Tuple<LocalDateTime, LocalDateTime>> availabilities = model.getAvailabilities();

        List<ActivityOffer> offers = activityOfferRepository.findAll();
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


    /**
     * Endpoint for checking if a participant is eligible to join a given activity.
     *
     * @param request wrapped in a ParticipantIsEligibleRequestModel.
     * @return boolean indicating eligibility.
     * @throws ResponseStatusException if not successful.
     */
    public boolean participantIsEligible(ParticipantIsEligibleRequestModel request, String authToken)
            throws ResponseStatusException {

        try {
            Optional<ActivityOffer> activityOffer = activityOfferRepository.findById(request.getActivityOfferId());

            if (activityOffer.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity with given ID not found");
            }

            // TODO(iannis): Retrieve certificates, gender, rank & organisation from user microservice.
            //               Return false if any of these don't match the activity offer requirements.
            HttpResponse<String> response = getUserDetails("http://localhost:8082/user/get/details/" + request.getParticipantNetid(), authToken);
            // Check if the request was successful
            if (response.statusCode() == HttpStatus.OK.value()) {
                // Parse the response body
                ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addDeserializer(TypesOfPositions.class, new TypesOfPositionsDeserializer());
                module.addDeserializer(Tuple.class, new TupleDeserializer());
                mapper.registerModule(module);

                List<CompetitionOffer> offers = activityOfferRepository.findAll().stream()
                        .filter(offer -> offer instanceof CompetitionOffer)
                        .map(offer -> (CompetitionOffer) offer)
                        .collect(Collectors.toList());

                UserDetailsModel model = mapper.readValue(response.body(), UserDetailsModel.class);
                CompetitionOffer competitionOffer = offers.get(0);

                boolean pro = model.isPro();
                if (pro != competitionOffer.isPro()) {
                    return false;
                }
                String organisation = model.getOrganisation();
                if (!organisation.equals(competitionOffer.getOrganisation())) {
                    return false;
                }
                // this is not entirely correct because we can have lower certificates which are still valid
                List<String> certificates = model.getCertificates();
                if (!certificates.contains(competitionOffer.getBoatCertificate())) {
                    return false;
                }
                String gender = model.getGender();
                if (gender.equals("MALE") && competitionOffer.isFemale()) {
                    return false;
                }
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while creating ActivityOffer. " + e.getMessage());
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

    public DataValidation getDataValidation() {
        return dataValidation;
    }

    public ActivityOfferRepository getActivityOfferRepository() {
        return activityOfferRepository;
    }
}
