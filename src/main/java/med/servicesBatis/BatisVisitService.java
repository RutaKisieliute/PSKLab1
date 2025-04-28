package med.servicesBatis;


import med.mybatis.model.Visit;
import med.mybatis.dao.VisitMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class BatisVisitService {

    @Inject
    private VisitMapper visitMapper;

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

        visitMapper.insert(visit);
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

        visitMapper.updateByPrimaryKey(visit);
        return true;
    }

    public Visit findById(Integer id) {
        return visitMapper.selectByPrimaryKey(id);
    }

    public List<Visit> getAllVisits() {
        return visitMapper.selectAllWithNames();
    }
}
