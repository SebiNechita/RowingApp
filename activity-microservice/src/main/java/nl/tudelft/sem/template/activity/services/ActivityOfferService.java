package nl.tudelft.sem.template.activity.services;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositionsDeserializer;
import nl.tudelft.sem.template.common.models.user.*;
import org.springframework.http.HttpStatus;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
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
    public List<ActivityOffer> getFilteredTrainings(NetId netId,String authToken) throws Exception{
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8082/user/get/details/"+ netId))
                    .header("Authorization", authToken)
                    .build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            List<ActivityOffer> offers = activityOfferRepository.findAll();
            List<ActivityOffer> filteredOffers = new ArrayList<>();

            // Check if the request was successful
            if (response.statusCode() == HttpStatus.OK.value()) {
                // Parse the response body
                ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addDeserializer(TypesOfPositions.class, new TypesOfPositionsDeserializer());
                module.addDeserializer(Tuple.class, new TupleDeserializer());
                mapper.registerModule(module);

                UserDetailsModel model = mapper.readValue(response.body(), UserDetailsModel.class);
                List<Tuple<LocalDateTime, LocalDateTime>> availabilities = model.getAvailabilities();

                for(ActivityOffer offer : offers){
                    for(Tuple<LocalDateTime, LocalDateTime> availability : availabilities){
                        if(!offer.getStartTime().isBefore(availability.getFirst()) && !offer.getEndTime().isAfter(availability.getSecond())){
                            filteredOffers.add(offer);
                        }
                    }
                }
                System.out.println(availabilities);
            } else {
                return List.of();
            }
            return filteredOffers;
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
    public boolean participantIsEligible(ParticipantIsEligibleRequestModel request, String authToken) throws ResponseStatusException {

         try {
             Optional<ActivityOffer> activityOffer = activityOfferRepository.findById(request.getActivityOfferId());

             if (activityOffer.isEmpty()) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity with given ID not found");
             }

             // TODO(iannis): Retrieve certificates, gender, rank & organisation from user microservice.
             //               Return false if any of these don't match the activity offer requirements.
             HttpClient httpClient = HttpClient.newHttpClient();

             HttpRequest request2 = HttpRequest.newBuilder()
                     .uri(URI.create("http://localhost:8082/user/get/details/" + request.getParticipantNetid()))
                     .header("Authorization", authToken)
                     .build();
             HttpResponse<String> response = httpClient.send(request2,
                     HttpResponse.BodyHandlers.ofString());

             List<CompetitionOffer> offers = activityOfferRepository.findAll().stream()
                     .filter(offer -> offer instanceof TrainingOffer)
                     .map(offer -> (CompetitionOffer) offer)
                     .collect(Collectors.toList());

             CompetitionOffer competitionOffer = (CompetitionOffer) offers.stream().filter(offer -> offer.getId() == request.getActivityOfferId());

             // Check if the request was successful
             if (response.statusCode() == HttpStatus.OK.value()) {
                 // Parse the response body
                 ObjectMapper mapper = new ObjectMapper();

                 SimpleModule module = new SimpleModule();
                 module.addDeserializer(TypesOfPositions.class, new TypesOfPositionsDeserializer());
                 module.addDeserializer(Tuple.class, new TupleDeserializer());
                 mapper.registerModule(module);

                 UserDetailsModel model = mapper.readValue(response.body(), UserDetailsModel.class);
                 //List<Tuple<LocalDateTime, LocalDateTime>> availabilities = model.getAvailabilities();
                 List<String> certificates = model.getCertificates();
                 String gender = model.getGender();
                 boolean pro = model.isPro();
                 String organisation = model.getOrganisation();

                 if (pro != competitionOffer.isPro())
                     return false;
                 if (!organisation.equals(competitionOffer.getOrganisation()))
                     return false;
                 // this is not entirely correct because we can have lower certificates which are still valid
                 if (!certificates.contains(competitionOffer.getBoatCertificate()))
                     return false;
                 if (gender.equals("MALE") && competitionOffer.isFemale())
                     return false;
                 return true;
             }
             return false;
            }   catch (Exception e){
             System.out.println("Exception in the service");
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating ActivityOffer. " + e.getMessage());
             }
         }
}
