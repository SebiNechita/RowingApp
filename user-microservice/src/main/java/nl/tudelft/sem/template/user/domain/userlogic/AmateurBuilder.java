package nl.tudelft.sem.template.user.domain.userlogic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.entities.AmateurUser;
import nl.tudelft.sem.template.user.domain.userlogic.entities.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.entities.PositionEntity;
import nl.tudelft.sem.template.user.domain.userlogic.entities.UserCertificate;

@NoArgsConstructor
public class AmateurBuilder implements UserBuilder {

    private transient NetId netId;
    private transient HashedPassword password;
    private transient Gender gender;
    private transient List<String> certificates;
    private transient TreeMap<LocalDateTime, LocalDateTime> availabilities;
    private transient List<TypesOfPositions> positions;
    private transient String organization;

    public AmateurBuilder(NetId netId,
                          HashedPassword password,
                          Gender gender,
                          List<String> certificates,
                          TreeMap<LocalDateTime, LocalDateTime> availabilities,
                          List<TypesOfPositions> positions,
                          String organization) {
        this.netId = netId;
        this.password = password;
        this.gender = gender;
        this.certificates = certificates;
        this.availabilities = availabilities;
        this.positions = positions;
        this.organization = organization;
    }

    @Override
    public void reset() {
        netId = null;
        password = null;
        gender = null;
        organization = null;
        certificates = new ArrayList<>();
        availabilities = new TreeMap<>();
        positions = new ArrayList<>();
    }

    @Override
    public void setNetId(NetId netId) {
        this.netId = netId;
    }

    @Override
    public void setPassword(HashedPassword password) {
        this.password = password;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public void setCertificates(List<String> certificates) {
        this.certificates = certificates;
    }

    @Override
    public void addCertificates(String certificate) {
        this.certificates.add(certificate);
    }

    @Override
    public void setAvailabilities(TreeMap<LocalDateTime, LocalDateTime> availabilities) {
        this.availabilities = availabilities;
    }

    @Override
    public void addAvailability(LocalDateTime start, LocalDateTime end) {
        this.availabilities.put(start, end);
    }

    @Override
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public void setPositions(List<TypesOfPositions> positions) {
        this.positions = positions;
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
