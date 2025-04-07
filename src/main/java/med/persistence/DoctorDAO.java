package med.persistence;

import med.entities.Doctor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class DoctorDAO {

    @Inject
    private EntityManager em;

    public void persist(Doctor doctor){
        this.em.persist(doctor);
    }

    public Doctor findOne(Integer id){
        return em.find(Doctor.class, id);
    }

    public Doctor update(Doctor doctor){
        return em.merge(doctor);
    }

    public List<Doctor> loadAll() {
        return em.createNamedQuery("Doctor.findAll", Doctor.class).getResultList();
    }
}