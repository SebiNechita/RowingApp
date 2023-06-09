package nl.tudelft.sem.template.common.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationRequestModel;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationResponseModel;
import nl.tudelft.sem.template.common.models.authentication.RegistrationRequestModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class AuthenticationMicroserviceAdapter {
    public final transient String authenticationMicroserviceAddress;

    /**
     * Instantiates a new AuthenticationMicroserviceAdapter with an injected microservice address.
     *
     * @param authenticationMicroserviceAddress The address of the microservice.
     */
    public AuthenticationMicroserviceAdapter(String authenticationMicroserviceAddress) {
        this.authenticationMicroserviceAddress = authenticationMicroserviceAddress;
    }


    /**
     * Instantiates a new AuthenticationMicroserviceAdapter.
     *
     */
    public AuthenticationMicroserviceAdapter() {
        this.authenticationMicroserviceAddress = MicroServiceAddresses.authenticationMicroservice;
    }

    private String authenticateEndpointUrl() {
        return authenticationMicroserviceAddress + "/authenticate";
    }

    private String registerEndpointUrl() {
        return authenticationMicroserviceAddress + "/register";
    }

    /**
     * Authenticate a user.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    public ResponseEntity<AuthenticationResponseModel> authenticate(AuthenticationRequestModel request)
            throws ResponseStatusException {
        return HttpUtils.sendHttpRequest(authenticateEndpointUrl(), HttpMethod.POST, request,
                AuthenticationResponseModel.class);
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netid already exists
     */
    public ResponseEntity<Void> register(RegistrationRequestModel request)
            throws ResponseStatusException {
        return HttpUtils.sendHttpRequest(registerEndpointUrl(), HttpMethod.POST, request,
                Void.class);
    }
}
