package med.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "MedicalHistory.findAll", query = "select a from MedicalHistory as a")
})
@Table(name = "MEDICAL_HISTORY")
@Getter
@Setter
public class MedicalHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @Column(name = "DISEASE")
    private String disease;

    @Column(name = "TREATMENT_START")
    private Date treatmentStart;

    @Size(max = 150)
    @Column(name = "TREATMENT_DEC")
    private String treatmentDec;

    @ManyToOne
    @JoinColumn(name = "patient_id") // matches the DB column
    private Patient patient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalHistory)) return false;
        MedicalHistory medicalHistory = (MedicalHistory) o;
        return id != null && id.equals(medicalHistory.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
