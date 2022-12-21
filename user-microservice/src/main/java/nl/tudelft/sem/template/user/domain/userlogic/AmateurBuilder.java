package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
public class AmateurBuilder implements UserBuilder {

    private transient NetId netId;
    private transient HashedPassword password;
    private transient Gender gender;
    private transient List<String> certificates;
    private transient TreeMap<LocalDateTime, LocalDateTime> availabilities;
    private transient List<TypesOfPositions> positions;

    public AmateurBuilder(NetId netId,
                          HashedPassword password,
                          Gender gender,
                          List<String> certificates,
                          TreeMap<LocalDateTime, LocalDateTime> availabilities,
                          List<TypesOfPositions> positions) {
        this.netId = netId;
        this.password = password;
        this.gender = gender;
        this.certificates = certificates;
        this.availabilities = availabilities;
        this.positions = positions;
    }

    @Override
    public void reset() {
        netId = null;
        password = null;
        gender = null;
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
    public void setPositions(List<TypesOfPositions> positions) {
        this.positions = positions;
    }

    @Override
    public void addPosition(TypesOfPositions position) {
        this.positions.add(position);
    }

    @Override
    public AmateurUser getUser() {
        return new AmateurUser(netId, password, gender, positions);
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

}
