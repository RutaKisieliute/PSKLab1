package med.services;

import med.entities.MedicalHistory;
import med.persistence.MedicalHistoryDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class MedicalHistoryService {

    @Inject
    private MedicalHistoryDAO medicalHistoryDAO;

    @Inject
    private Validator validator;

    public boolean createMedicalHistory(MedicalHistory medicalHistory) {
        Set<ConstraintViolation<MedicalHistory>> violations = validator.validate(medicalHistory);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<MedicalHistory> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        medicalHistoryDAO.persist(medicalHistory);
        return true;
    }

    public boolean updateMedicalHistory(MedicalHistory medicalHistory) {
        Set<ConstraintViolation<MedicalHistory>> violations = validator.validate(medicalHistory);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<MedicalHistory> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        medicalHistoryDAO.update(medicalHistory);
        return true;
    }

    public MedicalHistory findById(Integer id) {
        return medicalHistoryDAO.findOne(id);
    }

    public List<MedicalHistory> getAllMedicalHistories() {
        return medicalHistoryDAO.loadAll();
    }
}
