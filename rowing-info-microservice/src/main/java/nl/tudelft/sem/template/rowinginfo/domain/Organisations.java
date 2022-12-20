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
@Table(name = "organisations")
@ToString
public class Organisations {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Getter
    @Setter
    @NonNull
    @Column(name = "organisationName", nullable = false)
    private String organisationsName;


    /**
     * Initialises a Organisation without an Id.
     *
     * @param organisationsName   organisationsName
     */
    public Organisations(@NonNull String organisationsName) {
        this.organisationsName = organisationsName;
    }

}
