package nl.tudelft.sem.template.rowinginfo.controllers;

import java.util.List;
import nl.tudelft.sem.template.rowinginfo.domain.Certificates;
import nl.tudelft.sem.template.rowinginfo.models.CertificatesRequestModel;
import nl.tudelft.sem.template.rowinginfo.services.CertificatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CertificatesController {
    private final transient CertificatesService certificatesService;

    /**
     * Instantiates a new CertificatesController.
     *
     * @param certificatesService certificatesService
     */
    @Autowired
    public CertificatesController(CertificatesService certificatesService) {
        this.certificatesService = certificatesService;
    }

    /**
     * Endpoint for creating a new certificate.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/certificates")
    public ResponseEntity createNewCertificate(@RequestBody CertificatesRequestModel request) throws Exception {
        try {
            if (certificatesService.adminPermission()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            else {
                String certificateName = request.getCertificateName();
                int certificateValue = request.getCertificateValue();
                String description = request.getDescription();

                certificatesService.createCertificate(certificateName, certificateValue, description);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for getting all certificates offer.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @GetMapping("/get/certificates")
    public ResponseEntity<List<Certificates>> getAllCertificates() throws Exception {
        try {
            return ResponseEntity.ok(certificatesService.getAllCertificates());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for checking if certificates exists.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @GetMapping("/check/certificates/{certificateName}")
    public ResponseEntity<Boolean> checkCertificates(@PathVariable("certificateName") String certificateName)
            throws Exception {
        try {
            return ResponseEntity.ok(certificatesService.checkCertificates(certificateName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
