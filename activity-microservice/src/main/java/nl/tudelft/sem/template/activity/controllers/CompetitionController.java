package nl.tudelft.sem.template.activity.controllers;

import java.time.LocalDateTime;
import java.util.List;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.services.CompetitionOfferService;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.AvailableCompetitionsModel;
import nl.tudelft.sem.template.common.models.activity.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.NetId;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CompetitionController extends ActivityOfferController {

    private final transient CompetitionOfferService competitionOfferService;

    /**
     * Instantiates a new Competition Controller.
     *
     * @param userMicroserviceAdapter userMicroserviceAdapter
     */
    @Autowired
    public CompetitionController(CompetitionOfferService competitionOfferService,
                                 UserMicroserviceAdapter userMicroserviceAdapter) {
        super(userMicroserviceAdapter);
        this.competitionOfferService = competitionOfferService;
    }

    /**
     * Endpoint for creating a new competition offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/competition")
    public ResponseEntity createCompetition(@RequestBody CompetitionCreationRequestModel request,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken)
            throws Exception {
        try {
            TypesOfPositions position = request.getPosition();
            boolean isActive = request.isActive();
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime endTime = request.getEndTime();
            String ownerId = request.getOwnerId();
            String boatCertificate = request.getBoatCertificate();
            TypesOfActivities type = request.getType();
            String name = request.getName();
            String description = request.getDescription();
            String organisation = request.getOrganisation();
            boolean isFemale = request.isFemale();
            boolean isPro = request.isPro();

            competitionOfferService.createCompetitionOffer(position, isActive, startTime, endTime,
                    ownerId, boatCertificate, type, name, description, organisation, isFemale, isPro, authToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for checking if a participant is eligible to join a given activity.
     *
     * @param request wrapped in a ParticipantIsEligibleRequestModel.
     * @return boolean indicating eligibility.
     * @throws ResponseStatusException if not successful.
     */
    @PostMapping("/competition/participant-is-eligible")
    public ResponseEntity<Boolean> participantIsEligible(@RequestBody ParticipantIsEligibleRequestModel request,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken)
            throws ResponseStatusException {
        try {
            return ResponseEntity.ok(competitionOfferService.participantIsEligible(request, authToken));
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint for getting all competition offers.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @GetMapping("/get/competitions")
    public ResponseEntity<List<ActivityOffer>> getCompetition() throws Exception {
        try {
            return ResponseEntity.ok(competitionOfferService.getAllCompetitionOffers());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Get list of ActivityOffers that are active and are competitions and meet your requirements:
     * have the same gender as you do, are on the same experience level, are from the same organisation.
     *
     * @param netId netId
     * @return returns filtered competitions
     */
    @GetMapping("/get/competitions/filtered")
    public ResponseEntity<AvailableCompetitionsModel> getFilteredCompetitions(
            @RequestBody NetId netId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        try {
            UserDetailsModel request = super.userMicroserviceAdapter.getUserDetailsModel(netId, authToken).getBody();
            String organisation = request.getOrganisation();
            boolean isFemale = request.getGender().equals("FEMALE");
            boolean isPro = request.isPro();

            List<TypesOfPositions> positions = request.getPositions();
            List<Tuple<LocalDateTime, LocalDateTime>> availabilities = request.getAvailabilities();
            List<String> certificates = request.getCertificates();

            return ResponseEntity.ok(competitionOfferService.getFilteredCompetitionOffers(organisation, isFemale, isPro,
                    certificates, positions, availabilities));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
