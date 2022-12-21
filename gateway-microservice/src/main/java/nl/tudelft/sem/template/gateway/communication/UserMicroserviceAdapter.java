package nl.tudelft.sem.template.gateway.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMicroserviceAdapter {
    private final transient String userMicroserviceAddress;

    /**
     * Instantiates UserMicroserivceAdapter.
     */
    public UserMicroserviceAdapter() {
        this.userMicroserviceAddress = MicroServiceAddresses.userMicroservice;
    }

    /**
     * Provides a url for geting userId.
     *
     * @return Url
     */
    private String getUserIdUrl() {
        return userMicroserviceAddress + "/get/userId";
    }

    /**
     * Endpoint to get the UserId.
     *
     * @param authToken authToken
     * @return  userId
     */
    public ResponseEntity<String> getUserId(String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(getUserIdUrl(), HttpMethod.GET, authToken, "",
                String.class);
    }
}
