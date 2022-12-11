package nl.tudelft.sem.template.common.http;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static <RequestBodyType, ResponseBodyType> ResponseEntity<ResponseBodyType>
    sendHttpRequest(String url, HttpMethod method, RequestBodyType body, Map<String, String> headers,
                    Class<ResponseBodyType> responseBodyType) {
        var restTemplate = new RestTemplate();
        var reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            reqHeaders.set(header.getKey(), header.getValue());
        }

        return restTemplate.exchange(url, method, new HttpEntity<>(body), responseBodyType);
    }

    public static <RequestBodyType, ResponseBodyType> ResponseEntity<ResponseBodyType>
    sendHttpRequest(String url, HttpMethod method, RequestBodyType body, Class<ResponseBodyType> responseBodyType) {
        return sendHttpRequest(url, method, body, new HashMap<>(), responseBodyType);
    }
}
