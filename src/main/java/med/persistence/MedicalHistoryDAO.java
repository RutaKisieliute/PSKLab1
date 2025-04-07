package med.persistence;

import med.entities.Doctor;
import med.entities.MedicalHistory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class MedicalHistoryDAO {

    @Inject
    private EntityManager em;

    public void persist(MedicalHistory medicalHistory){
        this.em.persist(medicalHistory);
    }

    public MedicalHistory findOne(Integer id){
        return em.find(MedicalHistory.class, id);
    }

    public MedicalHistory update(MedicalHistory medicalHistory){return em.merge(medicalHistory);}

    public List<MedicalHistory> loadAll() {return em.createNamedQuery("MedicalHistory.findAll", MedicalHistory.class).getResultList();}
}