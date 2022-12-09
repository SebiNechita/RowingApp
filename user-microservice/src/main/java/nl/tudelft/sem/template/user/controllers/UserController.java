package nl.tudelft.sem.template.user.controllers;

import nl.tudelft.sem.template.user.authentication.AuthManager;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import nl.tudelft.sem.template.user.domain.userlogic.SetAccountDetailsService;
import nl.tudelft.sem.template.user.modules.SetAccountDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {
    private final transient AuthManager authManager;
    private final transient SetAccountDetailsService setAccountDetailsService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public UserController(AuthManager authManager, SetAccountDetailsService setAccountDetailsService) {
        this.authManager = authManager;
        this.setAccountDetailsService = setAccountDetailsService;
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @PostMapping ("/setAccountDetails")
    public ResponseEntity setAccountDetails(@RequestBody SetAccountDetailsModel request) throws Exception {

        try {
            NetId netId = new NetId(request.getNetId());
            Password password = new Password(request.getPassword());
            List<String> availabilitiesAsStrings = request.getAvailabilities();
            List<String> certificates = request.getCertificates();
            setAccountDetailsService.
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
