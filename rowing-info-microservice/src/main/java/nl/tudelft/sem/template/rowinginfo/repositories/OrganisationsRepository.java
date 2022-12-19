package nl.tudelft.sem.template.rowinginfo.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.rowinginfo.domain.Organisations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationsRepository extends JpaRepository<Organisations, Integer> {

    /**
     * Method to find Organisations in repository by its ID.
     *
     * @param id id
     * @return Certificates
     */
    Optional<Organisations> findById(int id);

    /**
     * Method to check if Organisations is in repository by its name.
     *
     * @param organisationName name
     * @return Boolean
     */
    Optional<Boolean> existsByOrganisationsName(String organisationName);
}
