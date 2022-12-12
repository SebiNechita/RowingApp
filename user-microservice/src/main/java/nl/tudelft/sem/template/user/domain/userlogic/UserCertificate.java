package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "usercertificate")
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

    public UserCertificate(NetId netId, String certificate) {
        this.netId = netId;
        this.certificate = certificate;
    }
}
