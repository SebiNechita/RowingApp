package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.communication.AuthenticationMicroserviceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationRequestModel;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationResponseModel;

/**
 * Gateway controller.
 */
@RestController
public class GatewayController {

    AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter;

    /**
     * Instantiates a new GatewayController.
     */
    @Autowired
    public GatewayController() {}

    /**
     * Gateway endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        return authenticationMicroserviceAdapter.authenticate(request);
    }
}
