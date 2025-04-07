package med.persistence;

import med.entities.Visit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class VisitDAO {

    @Inject
    private EntityManager em;

    public void persist(Visit visit){
        this.em.persist(visit);
    }

    public Visit findOne(Integer id){
        return em.find(Visit.class, id);
    }

    public Visit update(Visit visit){
        return em.merge(visit);
    }

    public List<Visit> loadAll() {
        return em.createNamedQuery("Visit.findAll", Visit.class).getResultList();
    }
}