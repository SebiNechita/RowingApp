package nl.tudelft.sem.template.user.domain.userlogic.events;

import nl.tudelft.sem.template.user.domain.userlogic.NetId;

public class UserWasCreatedEvent {
    private final NetId netId;

    public UserWasCreatedEvent(NetId netId) {
        this.netId = netId;
    }

    public NetId getNetId() {
        return this.netId;
    }
}
