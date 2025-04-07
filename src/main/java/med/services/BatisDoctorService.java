package med.services;

import lombok.Getter;
import lombok.Setter;
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
    private DoctorMapper doctorMapper;  // Inject MyBatis DoctorMapper

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

        doctorMapper.insert(doctor); // Insert doctor using MyBatis
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

        doctorMapper.updateByPrimaryKey(doctor); // Update doctor using MyBatis
        return true;
    }

    public Doctor findById(Integer id) {
        return doctorMapper.selectByPrimaryKey(id); // Fetch doctor by ID using MyBatis
    }

    public List<Doctor> getAllDoctors() {
        return doctorMapper.selectAll(); // Fetch all doctors using MyBatis
    }
}
