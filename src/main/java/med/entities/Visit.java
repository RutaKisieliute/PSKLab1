package med.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Visit.findAll", query = "select a from Visit as a")
})
@Table(name = "VISIT")
@Getter
@Setter
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @Column(name = "NAME")
    private String name;

    @Past(message = "Birth date must be in the past")
    @Column(name = "DATE")
    private Date date;

    @Size(max = 150)
    @Column(name = "NOTES")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "patient_id") // matches the DB column
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id") // matches the DB column
    private Doctor doctor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visit)) return false;
        Visit visit = (Visit) o;
        return id != null && id.equals(visit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
