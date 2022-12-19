package nl.tudelft.sem.template.common.http;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

public class HttpUtils {

    /**
     * Send a HTTP request to a given URL.
     *
     * @param url The URL to send the request to.
     * @param method The HTTP method to use.
     * @param body The body of the request.
     * @param headers The headers of the request.
     * @param responseBodyType The type of the response body.
     */
    public static <RequestBodyT, ResponseBodyT> ResponseEntity<ResponseBodyT> sendHttpRequest(
            String url, HttpMethod method, RequestBodyT body, Map<String, String> headers,
            Class<ResponseBodyT> responseBodyType) throws ResponseStatusException {
        var restTemplate = new RestTemplate();
        var reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON);

        for (var header : headers.entrySet()) {
            reqHeaders.set(header.getKey(), header.getValue());
        }

        try {
            return restTemplate.exchange(url, method, new HttpEntity<>(body, reqHeaders), responseBodyType);
        } catch (HttpStatusCodeException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    /**
     * Send a HTTP request to a given URL.
     *
     * @param url The URL to send the request to.
     * @param method The HTTP method to use.
     * @param body The body of the request.
     * @param responseBodyType The type of the response body.
     */
    public static <RequestBodyT, ResponseBodyT> ResponseEntity<ResponseBodyT> sendHttpRequest(
            String url, HttpMethod method, RequestBodyT body,
            Class<ResponseBodyT> responseBodyType) throws ResponseStatusException {
        return sendHttpRequest(url, method, body, new HashMap<>(), responseBodyType);
    }
}
