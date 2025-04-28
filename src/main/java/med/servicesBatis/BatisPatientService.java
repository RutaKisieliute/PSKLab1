package med.servicesBatis;

import med.mybatis.model.Patient;
import med.mybatis.dao.PatientMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class BatisPatientService {

    @Inject
    private PatientMapper patientMapper;

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

        patientMapper.insert(patient);
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

        patientMapper.updateByPrimaryKey(patient);
        return true;
    }

    public Patient findById(Integer id) {
        return patientMapper.selectByPrimaryKey(id);
    }

    public List<Patient> getAllPatients() {
        return patientMapper.selectAll();
    }
}
