package nl.tudelft.sem.template.common.models.rowinginfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CertificatesRequestModel {

    private String certificateName;
    private int certificateValue;
    private String description;

}
