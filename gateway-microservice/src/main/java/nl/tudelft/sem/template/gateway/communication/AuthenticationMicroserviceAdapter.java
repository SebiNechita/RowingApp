package nl.tudelft.sem.template.gateway.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import nl.tudelft.sem.template.authentication.models.AuthenticationRequestModel;
import nl.tudelft.sem.template.authentication.models.AuthenticationResponseModel;

@Component
public class AuthenticationMicroserviceAdapter {
    public final String authenticationMicroserviceAddress;

    /**
     * Instantiates a new AuthenticationMicroserviceAdapter.
     */
    public AuthenticationMicroserviceAdapter() {
        this.authenticationMicroserviceAddress = MicroServiceAddresses.authenticationMicroservice;
    }

    /**
     * Instantiates a new AuthenticationMicroserviceAdapter with an injected microservice address.
     * @param authenticationMicroserviceAddress The address of the microservice.
     */
    public AuthenticationMicroserviceAdapter(String authenticationMicroserviceAddress) {
        this.authenticationMicroserviceAddress = authenticationMicroserviceAddress;
    }

    private String getAuthenticateEndpointUrl() {
        return authenticationMicroserviceAddress + "/authenticate";
    }

    /**
     * Authenticate a user.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        return HttpUtils.sendHttpRequest(getAuthenticateEndpointUrl(), HttpMethod.POST, request,
                AuthenticationResponseModel.class);
    }
}
