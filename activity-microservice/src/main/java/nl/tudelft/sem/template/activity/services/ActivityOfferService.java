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

public class ActivityOfferService {

    private final transient ActivityOfferRepository activityOfferRepository;
    private final transient DataValidation dataValidation;
    private final transient ActivityClient activityClient;
    private final transient UserModelParser userModelParser;

    /**
     * Instantiates a new ActivityOfferService.
     *
     * @param activityOfferRepository activityOfferRepository
     * @param dataValidation          dataValidation
     */
    public ActivityOfferService(ActivityOfferRepository activityOfferRepository,
                                DataValidation dataValidation,
                                ActivityClient activityClient,
                                UserModelParser userModelParser) {
        this.activityOfferRepository = activityOfferRepository;
        this.dataValidation = dataValidation;
        this.activityClient = activityClient;
        this.userModelParser = userModelParser;
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
            HttpResponse<String> response = activityClient.getUserDetails("http://localhost:8082/user/get/details/" + request.getParticipantNetid(), authToken);
            // Check if the request was successful
            if (response.statusCode() == HttpStatus.OK.value()) {

                UserDetailsModel model = userModelParser.getModel(response);

                List<CompetitionOffer> offers = activityOfferRepository.findAll().stream()
                        .filter(offer -> offer instanceof CompetitionOffer)
                        .map(offer -> (CompetitionOffer) offer)
                        .collect(Collectors.toList());

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

    public DataValidation getDataValidation() {
        return dataValidation;
    }

    public ActivityOfferRepository getActivityOfferRepository() {
        return activityOfferRepository;
    }

    public ActivityClient getActivityClient() {
        return activityClient;
    }

    public UserModelParser getUserModelParser() {
        return userModelParser;
    }
}
