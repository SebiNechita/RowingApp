package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.common.communication.AuthenticationMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationRequestModel;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationResponseModel;
import nl.tudelft.sem.template.common.models.authentication.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GatewayAuthenticationController extends GatewayController {

    private final transient AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter;

    /**
     * Instantiates a new Gateway Activity Offer Controller.
     *
     * @param authenticationMicroserviceAdapter authenticationMicroserviceAdapter
     */
    @Autowired
    public GatewayAuthenticationController(AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter) {
        this.authenticationMicroserviceAdapter = authenticationMicroserviceAdapter;
    }

    /**
     * Gateway endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        logger.info("Received authentication request from user: " + request.getNetId());
        return authenticationMicroserviceAdapter.authenticate(request);
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws ResponseStatusException if a user with this netid already exists
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequestModel request) {
        logger.info("Received register request for user: " + request.getNetId());
        return authenticationMicroserviceAdapter.register(request);
    }

}
