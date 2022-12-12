package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user-certificate")
@NoArgsConstructor
public class UserCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "certificate")
    private String certificate;

    UserCertificate(NetId netId, String certificate, int importance) {
        this.netId = netId;
        this.certificate = certificate;
    }
}
