package nl.tudelft.sem.template.activity.services;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidOrganisationException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.common.communication.MicroServiceAddresses;
import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.rowingInfo.CertificatesExistanceRequestModel;
import nl.tudelft.sem.template.common.models.rowingInfo.OrganisationsRequestModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class DataValidation {

    public final transient String rowingInfoMicroserviceAddress;

    /**
     * Instantiates new DataValidation class.
     */
    public DataValidation() {
        this.rowingInfoMicroserviceAddress = MicroServiceAddresses.rowingInfoMicroservice;
    }

    /**
     * Return Url for checking if the certificate exists.
     *
     * @return Url
     */
    private String checkCertificateExistanceUrl() {
        return rowingInfoMicroserviceAddress + "/check/certificates";
    }

    /**
     * Return Url for checking if the organisation exists.
     *
     * @return Url
     */
    private String checkOrganisationExistanceUrl() {
        return rowingInfoMicroserviceAddress + "/check/organisations";
    }

    /**
     * Method that validates if the provided data is correct.
     *
     * @param startTime   startTime
     * @param endTime     endTime
     * @param name        name
     * @param description description
     * @param certificate certificate
     * @param authToken   authToken
     * @return isDataCorrect
     * @throws Exception Exception when something is not correct
     */
    public boolean validateData(LocalDateTime startTime, LocalDateTime endTime, String name, String description,
                                String certificate, String authToken) throws Exception {
        boolean isTimeOk = validateTime(startTime, endTime);
        boolean isNameDescriptionOk = validateNameAndDescription(name, description);
        boolean isCertificateOk = validateCertificate(certificate, authToken);

        return isTimeOk && isNameDescriptionOk && isCertificateOk;
    }

    /**
     * Validate time data.
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return isDataCorrect
     * @throws NotCorrectIntervalException Exception when startTime is not before endTime
     */
    public boolean validateTime(LocalDateTime startTime, LocalDateTime endTime) throws NotCorrectIntervalException {
        if (!startTime.isBefore(endTime)) {
            throw new NotCorrectIntervalException("Start time of the interval has to be before the end time.");
        }
        return true;
    }

    /**
     * Validate name and description.
     *
     * @param name        name
     * @param description description
     * @return isDataCorrect
     * @throws EmptyStringException Exception when name or description are empty
     */
    public boolean validateNameAndDescription(String name, String description) throws EmptyStringException {
        if (name.isEmpty()) {
            throw new EmptyStringException("Name");
        }
        if (description.isEmpty()) {
            throw new EmptyStringException("Description");
        }
        return true;
    }

    /**
     * Check if the certificate exists.
     *
     * @param certificate certificate
     * @param authToken   authToken
     * @return boolean doesCertificateExist
     */
    public boolean validateCertificate(String certificate, String authToken) throws InvalidCertificateException {
        CertificatesExistanceRequestModel request = new CertificatesExistanceRequestModel(certificate);
        if (Boolean.TRUE.equals(HttpUtils.sendAuthorizedHttpRequest(checkCertificateExistanceUrl(),
                HttpMethod.GET, authToken, request, boolean.class).getBody())) {
            return true;
        } else {
            throw new InvalidCertificateException(certificate);
        }
    }

    /**
     * Check if the organisation exists.
     *
     * @param organisation organisation
     * @param authToken    authToken
     * @return boolean doesOrganisationExist
     */
    public boolean validateOrganisation(String organisation, String authToken) throws InvalidOrganisationException {
        OrganisationsRequestModel request = new OrganisationsRequestModel(organisation);
        if (Boolean.TRUE.equals(HttpUtils.sendAuthorizedHttpRequest(checkOrganisationExistanceUrl(),
                HttpMethod.GET, authToken, request, boolean.class).getBody())) {
            return true;
        } else {
            throw new InvalidOrganisationException(organisation);
        }
    }
}
