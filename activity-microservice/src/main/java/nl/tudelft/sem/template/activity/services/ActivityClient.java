package nl.tudelft.sem.template.activity.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class ActivityClient {

    /**
     * Send request to the User microservice to get the user details.
     *
     * @param netId     netId
     * @param authToken authToken
     * @return HttpResponse         HttpResponse
     * @throws IOException          exception
     * @throws InterruptedException exceptions
     */
    public HttpResponse<String> getUserDetails(String netId, String authToken)
            throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(netId))
                .header("Authorization", authToken)
                .build();
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

}
