package nl.tudelft.sem.template.example.controllers;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.example.domain.TypesOfActivities;
import nl.tudelft.sem.template.example.models.TrainingCreationRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@NoArgsConstructor
public class ActivityOfferController {

    /**
     * created offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/training")
    public ResponseEntity createOffer(@RequestBody TrainingCreationRequestModel request) throws Exception {
        try {
            System.out.println(" siema");
        //            String position = request.getPosition();
        //            boolean isActive = request.isActive();
        //            LocalDateTime startTime = request.getStartTime();
        //            LocalDateTime endTime = request.getEndTime();
        //            String ownerId = request.getOwnerId();
        //            String boatCertificate = request.getBoatCertificate();
        //            TypesOfActivities type = request.getType();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
