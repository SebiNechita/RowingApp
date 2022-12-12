package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "importance")
    private int importance;

    UserCertificate(NetId netId, String certificate, int importance) {
        this.netId = netId;
        this.certificate = certificate;
        this.importance = importance;
    }

    public static List<UserCertificate> generateUserCertificates(List<String> certificatesAsStrings) throws Exception{
        List<UserCertificate> certificates = new ArrayList<>();
        try {
            for(String currentCertificate : certificatesAsStrings) {
                switch (currentCertificate) {
                    case "C4": certificates.add(UserCertificate.CFour);
                    case "4+": certificates.add(UserCertificate.FourPlus);
                    case "8+": certificates.add(UserCertificate.EightPlus);
                    default: throw new InvalidCertificateException();
                }
            }
        } catch (InvalidCertificateException e) {
            System.out.println(e.getMessage());
        }
        return certificates;
    }

}
