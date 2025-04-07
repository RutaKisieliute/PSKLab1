package med.usecases;

import lombok.Getter;
import lombok.Setter;
import med.mybatis.model.Doctor;  // Use MyBatis Doctor model
import med.services.BatisDoctorService;  // Inject BatisDoctorService

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Model
@ViewScoped
public class BatisDoctorUsecase implements Serializable {

    @Inject
    private BatisDoctorService doctorService;  // Inject the new BatisDoctorService

    @Getter @Setter
    private Doctor doctor = new Doctor(); // For creating or editing a doctor

    @Getter @Setter
    private Integer doctorId; // Passed from request params (for edit)

    @Getter
    private List<Doctor> allDoctors;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String idParam = params.get("doctorId");

        if (idParam != null) {
            try {
                this.doctorId = Integer.parseInt(idParam);
                this.doctor = doctorService.findById(doctorId); // Fetch doctor by ID using MyBatis
            } catch (NumberFormatException e) {
                System.err.println("Invalid doctorId: " + idParam);
            }
        }

        // Load all doctors for listing, optional
        this.allDoctors = doctorService.getAllDoctors(); // Get all doctors using MyBatis
    }

    @Transactional
    public String createDoctor() {
        if (doctorService.createDoctor(doctor)) {
            return "batisDoctorList?faces-redirect=true";
        }
        return null; // Stay on the same page if creation fails
    }

    @Transactional
    public String updateDoctor() {
        if (doctorService.updateDoctor(doctor)) {
            return "batisDoctorList?faces-redirect=true";
        }
        return null; // Stay on the same page if update fails
    }

    public String loadDoctorForUpdate() {
        if (doctorId != null) {
            this.doctor = doctorService.findById(doctorId); // Load doctor to edit
            return "batisEditDoctor?faces-redirect=true";
        }
        return null; // Redirect to create page if no doctorId
    }
}
