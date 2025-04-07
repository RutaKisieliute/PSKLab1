package med.services;

import med.entities.Patient;
import med.persistence.PatientDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class PatientService {

    @Inject
    private PatientDAO patientDAO;

    @Inject
    private Validator validator;

    @Transactional
    public boolean createPatient(Patient patient) {
        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Patient> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        patientDAO.persist(patient);
        return true;
    }

    @Transactional
    public boolean updatePatient(Patient patient) {
        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Patient> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        patientDAO.update(patient);
        return true;
    }

    public Patient findById(Integer id) {
        return patientDAO.findOne(id);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.loadAll();
    }
}
