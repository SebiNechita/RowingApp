package nl.tudelft.sem.template.common.models.activity_match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingOffersResponseModel {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinRequest {
        private String netId;
    }

    private List<JoinRequest> joinRequests;
}
