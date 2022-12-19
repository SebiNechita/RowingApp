package nl.tudelft.sem.template.rowinginfo.services;

import java.util.List;
import nl.tudelft.sem.template.rowinginfo.domain.Organisations;
import nl.tudelft.sem.template.rowinginfo.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.rowinginfo.repositories.OrganisationsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganisationsService {
    private final transient OrganisationsRepository organisationsRepository;

    /**
     * Instantiates a new OrganisationsService.
     *
     * @param organisationsRepository organisationsRepository
     */
    public OrganisationsService(OrganisationsRepository organisationsRepository) {
        this.organisationsRepository = organisationsRepository;
    }

    /**
     * Creates a new Organisations and adds it to database.
     *
     * @param name            name
     * @throws Exception EmptyStringException
     */
    public void createOrganisations(String name) throws Exception {
        if (name.isEmpty()) {
            throw new EmptyStringException("Name");
        }
        if (checkOrganisations(name)) {
            throw new Exception("Certificate already exists");
        }
        organisationsRepository.save(new Organisations(name));
    }

    /**
     * Gets a list of Organisations.
     *
     * @throws Exception exception
     */
    public List<Organisations> getAllOrganisations() throws Exception {
        try {
            return organisationsRepository.findAll();
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating Organisations. " + e.getMessage());
        }
    }

    /**
     * Checks if a Organisations exist.
     *
     * @throws Exception exception
     */
    public Boolean checkOrganisations(String name) throws Exception {
        try {
            return organisationsRepository.existsByOrganisationsName(name).orElseThrow();
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating Certificates. " + e.getMessage());
        }
    }

}
