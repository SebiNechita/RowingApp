package nl.tudelft.sem.template.user.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import nl.tudelft.sem.template.user.authentication.AuthManager;
import nl.tudelft.sem.template.user.domain.userlogic.*;
import nl.tudelft.sem.template.user.domain.userlogic.services.AmateurAccountDetailsService;
import nl.tudelft.sem.template.user.models.AmateurSetAccountDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {
    private final transient AuthManager authManager;
    private final transient AmateurAccountDetailsService amateurAccountDetailsService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public UserController(AuthManager authManager, AmateurAccountDetailsService amateurAccountDetailsService) {
        this.authManager = authManager;
        this.amateurAccountDetailsService = amateurAccountDetailsService;
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @PostMapping ("amateur/set/account/details")
    public ResponseEntity setAccountDetails(@RequestBody AmateurSetAccountDetailsModel request) throws Exception {

        try {
            NetId netId = new NetId(request.getNetId());
            Password password = new Password(request.getPassword());
            Gender gender = Gender.valueOf(request.getGender());
            TreeMap<LocalDateTime, LocalDateTime> availabilities =
                    Availability.generateAvailabilities(request.getAvailabilities());
            List<String> certificates = request.getCertificates();
            List<TypesOfPositions> positions = request.getPositions();
            amateurAccountDetailsService.setAccountDetails(netId, password, gender, positions, availabilities, certificates);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok("Account (" + authManager.getNetId() + ") set successfully");
    }
}
