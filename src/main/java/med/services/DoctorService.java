package med.services;

import lombok.Getter;
import lombok.Setter;
import med.entities.Doctor;
import med.persistence.DoctorDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RequestScoped
public class DoctorService {

    @Inject
    private DoctorDAO doctorDAO;

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

        doctorDAO.persist(doctor);
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

        doctorDAO.update(doctor);
        return true;
    }

    public Doctor findById(Integer id) {
        return doctorDAO.findOne(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.loadAll();
    }
}
