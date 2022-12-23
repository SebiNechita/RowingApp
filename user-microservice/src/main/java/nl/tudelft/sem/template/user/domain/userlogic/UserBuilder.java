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

public interface UserBuilder<T extends User> {

    UserBuilder<T> setNetId(NetId netId);

    UserBuilder<T> setPassword(HashedPassword password);

    UserBuilder<T> setGender(Gender gender);

    UserBuilder<T> setCertificates(List<String> certificates);

    UserBuilder<T> addCertificates(String certificate);

    UserBuilder<T> setAvailabilities(TreeMap<LocalDateTime, LocalDateTime> availabilities);

    UserBuilder<T> addAvailability(LocalDateTime start, LocalDateTime end);

    UserBuilder<T> setPositions(List<TypesOfPositions> positions);

    UserBuilder<T> setOrganization(String organization);

    User getUser();

    List<Availability> getAvailabilities();

    Set<UserCertificate> getCertificates();

    List<PositionEntity> getPositions();
}
