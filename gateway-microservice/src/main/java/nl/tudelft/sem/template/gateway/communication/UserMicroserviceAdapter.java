package nl.tudelft.sem.template.gateway.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMicroserviceAdapter {
    private final transient String userMicroserviceAddress;


    public UserMicroserviceAdapter() {
        this.userMicroserviceAddress = MicroServiceAddresses.userMicroservice;
    }

    private String getUserIdUrl() {return userMicroserviceAddress + "/get/userId";}

    /**
     * Endpoint to get the UserId
     *
     * @param authToken authToken
     * @return
     */
    public ResponseEntity<String> getUserId(String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(getUserIdUrl(), HttpMethod.GET, authToken, "",
                String.class);
    }
}
