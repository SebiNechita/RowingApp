package nl.tudelft.sem.template.user.domain.userlogic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.entities.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.entities.PositionEntity;
import nl.tudelft.sem.template.user.domain.userlogic.entities.User;
import nl.tudelft.sem.template.user.domain.userlogic.entities.UserCertificate;

public interface UserBuilder {
    void reset();

    void setNetId(NetId netId);

    void setPassword(HashedPassword password);

    void setGender(Gender gender);

    void setCertificates(List<String> certificates);

    void addCertificates(String certificate);

    void setAvailabilities(TreeMap<LocalDateTime, LocalDateTime> availabilities);

    void addAvailability(LocalDateTime start, LocalDateTime end);

    void setPositions(List<TypesOfPositions> positions);

    void setOrganization(String organization);

    User getUser();

    List<Availability> getAvailabilities();

    Set<UserCertificate> getCertificates();

    Set<PositionEntity> getPositions();
}
