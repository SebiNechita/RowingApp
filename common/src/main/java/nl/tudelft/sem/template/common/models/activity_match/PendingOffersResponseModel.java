package nl.tudelft.sem.template.common.models.activity_match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
public class PendingOffersResponseModel {
    @Data
    @AllArgsConstructor
    public static class JoinRequest {
        @Getter
        private String netId;
    }

    @Getter
    private List<JoinRequest> joinRequests;
}
