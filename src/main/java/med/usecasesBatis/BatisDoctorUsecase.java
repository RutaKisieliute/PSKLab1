package med.usecasesBatis;

import lombok.Getter;
import lombok.Setter;
import med.mybatis.model.Doctor;
import med.servicesBatis.BatisDoctorService;

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
    private BatisDoctorService doctorService;

    @Getter @Setter
    private Doctor doctor = new Doctor();

    @Getter @Setter
    private Integer doctorId;

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

        // Call the service method to retrieve doctors with visits
        this.allDoctors = doctorService.findDoctorWithVisits();
    }

    @Transactional
    public String createDoctor() {
        if (doctorService.createDoctor(doctor)) {
            return "batisDoctorList?faces-redirect=true";
        }
        return null;
    }

    @Transactional
    public String updateDoctor() {
        if (doctorService.updateDoctor(doctor)) {
            return "batisDoctorList?faces-redirect=true";
        }
        return null;
    }

    public String loadDoctorForUpdate() {
        if (doctorId != null) {
            this.doctor = doctorService.findById(doctorId);
            return "batisEditDoctor?faces-redirect=true";
        }
        return null;
    }

    // Add this getter to make it accessible to JSF
    public List<Doctor> getFindDoctorWithVisits() {
        return doctorService.findDoctorWithVisits();
    }

}
