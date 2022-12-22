package nl.tudelft.sem.template.user.domain.userlogic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.entities.*;

@AllArgsConstructor
@NoArgsConstructor
public class AmateurBuilder implements UserBuilder<AmateurUser> {

    private transient NetId netId;
    private transient HashedPassword password;
    private transient Gender gender;
    private transient List<String> certificates;
    private transient TreeMap<LocalDateTime, LocalDateTime> availabilities;
    private transient List<TypesOfPositions> positions;
    private transient String organization;

    @Override
    public UserBuilder<AmateurUser> setNetId(NetId netId) {
        this.netId = netId;
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> setPassword(HashedPassword password) {
        this.password = password;
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> setCertificates(List<String> certificates) {
        this.certificates = certificates;
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> addCertificates(String certificate) {
        this.certificates.add(certificate);
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> setAvailabilities(TreeMap<LocalDateTime, LocalDateTime> availabilities) {
        this.availabilities = availabilities;
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> addAvailability(LocalDateTime start, LocalDateTime end) {
        this.availabilities.put(start, end);
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    @Override
    public UserBuilder<AmateurUser> setPositions(List<TypesOfPositions> positions) {
        this.positions = positions;
        return this;
    }


    @Override
    public AmateurUser getUser() {
        return new AmateurUser(netId, password, gender, organization);
    }

    @Override
    public List<Availability> getAvailabilities() {
        List<Availability> resultedAvailabilities = new ArrayList<>();
        for (Map.Entry<LocalDateTime, LocalDateTime> entry : availabilities.entrySet()) {
            LocalDateTime start = entry.getKey();
            LocalDateTime end = entry.getValue();
            resultedAvailabilities.add(new Availability(netId, start, end));
        }
        return resultedAvailabilities;
    }

    @Override
    public Set<UserCertificate> getCertificates() {
        Set<UserCertificate> noDuplicateCertificates = new HashSet<>();
        for (String certificate : certificates) {
            noDuplicateCertificates.add(new UserCertificate(netId, certificate));
        }
        return noDuplicateCertificates;
    }

    @Override
    public List<PositionEntity> getPositions() {
        return positions.stream()
                .distinct()
                .map(x -> new PositionEntity(netId, x))
                .collect(Collectors.toList());
    }

}
