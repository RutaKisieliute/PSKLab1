package med.usecases;

import lombok.Getter;
import lombok.Setter;
import med.entities.Doctor;
import med.services.DoctorService;

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
public class DoctorUsecase implements Serializable {

    @Inject
    private DoctorService doctorService;

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
                this.doctor = doctorService.findById(doctorId);
            } catch (NumberFormatException e) {
                System.err.println("Invalid doctorId: " + idParam);
            }
        }

        // Load all doctors for listing, optional
        this.allDoctors = doctorService.getAllDoctors();
    }

    @Transactional
    public String createDoctor() {
        if (doctorService.createDoctor(doctor)) {
            return "doctorList?faces-redirect=true";
        }
        return null; // Stay on the same page
    }

    @Transactional
    public String updateDoctor() {
        if (doctorService.updateDoctor(doctor)) {
            return "doctorList?faces-redirect=true";
        }
        return null;
    }

    public String loadDoctorForUpdate() {
        if (doctorId != null) {
            this.doctor = doctorService.findById(doctorId);
            return "editDoctor?faces-redirect=true";
        }
        return null;
    }
}
