package med.services;


import med.entities.Visit;
import med.persistence.VisitDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class VisitService {

    @Inject
    private VisitDAO visitDAO;

    @Inject
    private Validator validator;

    @Transactional
    public boolean createVisit(Visit visit) {
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Visit> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        visitDAO.persist(visit);
        return true;
    }

    @Transactional
    public boolean updateVisit(Visit visit) {
        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Visit> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        visitDAO.update(visit);
        return true;
    }

    public Visit findById(Integer id) {
        return visitDAO.findOne(id);
    }

    public List<Visit> getAllVisits() {
        return visitDAO.loadAll();
    }
}
