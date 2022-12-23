package nl.tudelft.sem.template.rowinginfo.controllers;

import nl.tudelft.sem.template.rowinginfo.domain.Organisations;
import nl.tudelft.sem.template.rowinginfo.models.OrganisationsRequestModel;
import nl.tudelft.sem.template.rowinginfo.repositories.OrganisationsRepository;
import nl.tudelft.sem.template.rowinginfo.services.OrganisationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrganisationsController {
    private final transient OrganisationsService organisationsService;
    private final OrganisationsRepository organisationsRepository;
    /**
     * Instantiates a new OrganisationsController.
     *
     * @param organisationsService organisationsService
     */
    @Autowired
    public OrganisationsController(OrganisationsService organisationsService, OrganisationsRepository organisationsRepository) {
        this.organisationsService = organisationsService;
        this.organisationsRepository = organisationsRepository;

    }

    /**
     * Endpoint for creating a new organisation.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/organisations")
    public ResponseEntity createNewOrganisation(@RequestBody OrganisationsRequestModel request) throws Exception {
        try {
            if (!organisationsService.adminPermission()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            else {
                String organisationsName = request.getOrganisationsName();

                organisationsService.createOrganisations(organisationsName);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for deleting an organisation.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @DeleteMapping("/delete/organisations/{organisationId}")
    public ResponseEntity<String> deleteCertificate(@PathVariable("organisationId") int organisationId) throws Exception {
        try {
            if (!organisationsService.adminPermission()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            else {
                Organisations organisations = organisationsRepository.findById(organisationId).orElseThrow();
                organisationsService.deleteOrganisation(organisationId);
                return ResponseEntity.ok(organisations.getOrganisationsName());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for getting all organisations offer.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @GetMapping("/get/organisations")
    public ResponseEntity<List<Organisations>> getAllOrganisations() throws Exception {
        try {
            return ResponseEntity.ok(organisationsService.getAllOrganisations());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for checking if organisations exists.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @GetMapping("/check/organisations/{organisationsName}")
    public ResponseEntity<Boolean> checkOrganisations(@PathVariable("organisationsName") String organisationsName)
            throws Exception {
        try {
            return ResponseEntity.ok(organisationsService.checkOrganisations(organisationsName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
