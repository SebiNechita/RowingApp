package nl.tudelft.sem.template.common.models.activitymatch;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
