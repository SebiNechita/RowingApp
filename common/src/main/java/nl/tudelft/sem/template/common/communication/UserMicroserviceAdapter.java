package nl.tudelft.sem.template.common.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.user.GetUserDetailsModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class UserMicroserviceAdapter {

    public final transient String userMicroserviceAddress;

    /**
     * Instantiates a new UserMicroserviceAdapter.
     */
    public UserMicroserviceAdapter() {
        this.userMicroserviceAddress = MicroServiceAddresses.userMicroservice;
    }

    /**
     * Instantiates a new UserMicroserviceAdapter with an injected microservice address.
     *
     * @param userMicroserviceAddress The address of the microservice.
     */
    public UserMicroserviceAdapter(String userMicroserviceAddress) {
        this.userMicroserviceAddress = userMicroserviceAddress;
    }

    private String getUserDetailsEndpointUrl(String userNetId) {
        return String.format("%s/user/get/details/%s", userMicroserviceAddress, userNetId);
    }

    /**
     * Gets the details of a user given by its NetID.
     *
     * @param netId The NetID of the user.
     * @return The details of the user.
     * @throws Exception if something goes wrong.
     */
    public ResponseEntity<GetUserDetailsModel> getUserDetails(NetId netId, String authToken)
            throws ResponseStatusException {
        return HttpUtils.sendAuthorizedHttpRequest(getUserDetailsEndpointUrl(netId.toString()), HttpMethod.GET, authToken,
                null, GetUserDetailsModel.class);
    }
}
