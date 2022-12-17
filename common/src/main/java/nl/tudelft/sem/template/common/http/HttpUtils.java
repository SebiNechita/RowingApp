package nl.tudelft.sem.template.common.http;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class HttpUtils {

    public static <RequestBodyType, ResponseBodyType> ResponseEntity<ResponseBodyType>
    sendHttpRequest(String url, HttpMethod method, RequestBodyType body, Map<String, String> headers,
                    Class<ResponseBodyType> responseBodyType) throws ResponseStatusException {
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

    public static <RequestBodyType, ResponseBodyType> ResponseEntity<ResponseBodyType>
    sendHttpRequest(String url, HttpMethod method, RequestBodyType body,
            Class<ResponseBodyType> responseBodyType) throws ResponseStatusException {
        return sendHttpRequest(url, method, body, new HashMap<>(), responseBodyType);
    }
}
