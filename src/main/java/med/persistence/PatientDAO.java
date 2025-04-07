package med.persistence;

import med.entities.Patient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class PatientDAO {

    @Inject
    private EntityManager em;

    public void persist(Patient patient){
        this.em.persist(patient);
    }

    public Patient findOne(Integer id){
        return em.find(Patient.class, id);
    }

    public Patient update(Patient patient){
        return em.merge(patient);
    }

    public List<Patient> loadAll() {
        return em.createNamedQuery("Patient.findAll", Patient.class).getResultList();
    }
}