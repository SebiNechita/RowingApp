package nl.tudelft.sem.template.common.models.authentication;

import lombok.Data;

/**
 * Model representing a registration request.
 */
@Data
public class RegistrationRequestModel {
    private String netId;
    private String password;
}