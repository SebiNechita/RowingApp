package nl.tudelft.sem.template.rowinginfo.services;

import java.util.List;
import nl.tudelft.sem.template.rowinginfo.domain.Certificates;
import nl.tudelft.sem.template.rowinginfo.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.rowinginfo.repositories.CertificatesRepository;
import org.springframework.stereotype.Service;

@Service
public class CertificatesService {
    private final transient CertificatesRepository certificatesRepository;

    /**
     * Instantiates a new CertificatesService.
     *
     * @param certificatesRepository certificatesRepository
     */
    public CertificatesService(CertificatesRepository certificatesRepository) {
        this.certificatesRepository = certificatesRepository;
    }

    /**
     * Creates a new Certificates and adds it to database.
     *
     * @param name            name
     * @param value           value
     * @param description     description
     * @throws Exception EmptyStringException
     */
    public void createCertificate(String name, int value, String description) throws Exception {
        if (name.isEmpty()) {
            throw new EmptyStringException("Name");
        }
        if (description.isEmpty()) {
            throw new EmptyStringException("Description");
        }
        certificatesRepository.save(new Certificates(name, value, description));
    }

    /**
     * Gets a list of Certificates.
     *
     * @throws Exception exception
     */
    public List<Certificates> getAllCertificates() throws Exception {
        try {
            return certificatesRepository.findAll();
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating Certificates. " + e.getMessage());
        }
    }
}
