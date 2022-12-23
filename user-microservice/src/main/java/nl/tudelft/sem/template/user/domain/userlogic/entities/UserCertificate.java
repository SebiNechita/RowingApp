package nl.tudelft.sem.template.user.domain.userlogic.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.converters.NetIdAttributeConverter;

@Entity
@Table(name = "userCertificate")
@Getter
@NoArgsConstructor
public class UserCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "certificate")
    private String certificate;

    public UserCertificate(@NonNull NetId netId, String certificate) {
        this.netId = netId;
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        return certificate;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCertificate that = (UserCertificate) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, netId, certificate);
    }
    /*
    Method that compare the attributes of two UserCertificates but not the id.

    public boolean equalAttributes(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCertificate that = (UserCertificate) o;
        return this.netId.equals(that.netId) && this.certificate.equals(that.certificate);
    }
    */
}
