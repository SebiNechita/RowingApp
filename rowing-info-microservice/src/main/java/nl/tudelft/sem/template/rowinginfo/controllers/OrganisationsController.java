package nl.tudelft.sem.template.rowinginfo.controllers;

import java.util.List;
import nl.tudelft.sem.template.rowinginfo.domain.Organisations;
import nl.tudelft.sem.template.rowinginfo.models.OrganisationsRequestModel;
import nl.tudelft.sem.template.rowinginfo.services.OrganisationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OrganisationsController {
    private final transient OrganisationsService organisationsService;

    /**
     * Instantiates a new OrganisationsController.
     *
     * @param organisationsService organisationsService
     */
    @Autowired
    public OrganisationsController(OrganisationsService organisationsService) {
        this.organisationsService = organisationsService;
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
            String organisationsName = request.getOrganisationsName();

            organisationsService.createOrganisations(organisationsName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Boolean> checkOrganisations(@PathVariable("organisationsName") String organisationsName) throws Exception {
        try {
            return ResponseEntity.ok(organisationsService.checkOrganisations(organisationsName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
