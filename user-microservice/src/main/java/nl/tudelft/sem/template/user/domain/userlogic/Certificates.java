package nl.tudelft.sem.template.user.domain.userlogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum Certificates  implements Serializable {
    CFour(1),
    FourPlus(2),
    EightPlus(3);

    //fields
    private int importance;

    Certificates(int importance) {
        this.importance = importance;
    }

    public static List<Certificates> generateCertificates(List<String> certificatesAsStrings) throws Exception{
        List<Certificates> certificates = new ArrayList<>();
        try {
            for(String currentCertificate : certificatesAsStrings) {
                switch (currentCertificate) {
                    case "C4": certificates.add(Certificates.CFour);
                    case "4+": certificates.add(Certificates.FourPlus);
                    case "8+": certificates.add(Certificates.EightPlus);
                    default: throw new InvalidCertificateException();
                }
            }
        } catch (InvalidCertificateException e) {
            System.out.println(e.getMessage());
        }
        return certificates;
    }

}
