package nl.tudelft.sem.template.rowinginfo.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.rowinginfo.domain.Certificates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificatesRepository extends JpaRepository<Certificates, Integer> {

    /**
     * Method to find Certificates in repository by its ID.
     *
     * @param id id
     * @return Certificates
     */
    Optional<Certificates> findById(int id);

    /**
     * Method to check if Certificates is in repository by its name.
     *
     * @param certificateName name
     * @return Boolean
     */
    Optional<Boolean> existsByCertificateName(String certificateName);
}
