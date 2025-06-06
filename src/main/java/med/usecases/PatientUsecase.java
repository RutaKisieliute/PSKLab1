package med.usecases;

import lombok.Getter;
import lombok.Setter;
import med.entities.Patient;
import med.services.PatientService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class PatientUsecase implements Serializable {

    @Inject
    private PatientService patientService;

    @Getter @Setter
    private Patient patient = new Patient();

    @Getter @Setter
    private Integer patientId;

    @Getter
    private List<Patient> allPatients;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String idParam = params.get("patientId");

        if (idParam != null) {
            try {
                this.patientId = Integer.parseInt(idParam);
                this.patient = patientService.findById(patientId);
            } catch (NumberFormatException e) {
                System.err.println("Invalid patientId: " + idParam);
                this.patient = new Patient();
            }
        }

        this.allPatients = patientService.getAllPatients();
    }

    public String createPatient() {
        if (patientService.createPatient(patient)) {
            return "patientList?faces-redirect=true";
        }
        return null;
    }

    public String updatePatient() {
        if (patientService.updatePatient(patient)) {
            return "patientList?faces-redirect=true";
        }
        return null;
    }

    public void loadPatientForUpdate() {
        if (patientId != null) {
            this.patient = patientService.findById(patientId);
        }
    }
}
