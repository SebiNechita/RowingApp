package nl.tudelft.sem.template.common.communication;

public class MicroServiceAddresses {
    public static final String hostname = "http://localhost";

    public static final String authenticationMicroservice = hostname + ":8081";
    public static final String rowingInfoMicroservice     = hostname + ":8088";
    public static final String activityOfferMicroservice  = hostname + ":8083";
    public static final String activityMatchMicroservice  = hostname + ":8086";
    public static final String userMicroservice           = hostname + ":8085";
}
