package med.servicesBatis;

import med.mybatis.model.Doctor;
import med.mybatis.dao.DoctorMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class BatisDoctorService {

    @Inject
    private DoctorMapper doctorMapper;

    @Inject
    private Validator validator;

    @Transactional
    public boolean createDoctor(Doctor doctor) {
        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Doctor> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        doctorMapper.insert(doctor);
        return true;
    }

    @Transactional
    public boolean updateDoctor(Doctor doctor) {
        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Doctor> v : violations) {
                System.out.println("Validation error: " + v.getPropertyPath() + " " + v.getMessage());
            }
            return false;
        }

        doctorMapper.updateByPrimaryKey(doctor);
        return true;
    }

    public Doctor findById(Integer id) {
        return doctorMapper.selectByPrimaryKey(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorMapper.selectAll();
    }

    public List<Doctor> findDoctorWithVisits() {
        return doctorMapper.findDoctorWithVisits();
    }
}
