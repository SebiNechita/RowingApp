package nl.tudelft.sem.template.rowinginfo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "certificates")
@ToString
public class Certificates {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Getter
    @Setter
    @NonNull
    @Column(name = "certificateName", nullable = false)
    private String certificateName;

    @Getter
    @Column(name = "certificateValue", nullable = false)
    private int certificateValue;

    @Getter
    @NonNull
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Initialises a Certificate without an Id.
     *
     * @param certificateName   certificateName
     * @param certificateValue  certificateValue
     * @param description       description
     */
    public Certificates(@NonNull String certificateName,
                        int certificateValue,
                        @NonNull String description) {
        this.certificateName = certificateName;
        this.certificateValue = certificateValue;
        this.description = description;
    }

    /**
     * Initialises a Certificate without an Id and description.
     *
     * @param certificateName   certificateName
     * @param certificateValue  certificateValue
     */
    public Certificates(@NonNull String certificateName,
                        int certificateValue) {
        this.certificateName = certificateName;
        this.certificateValue = certificateValue;
    }
}
