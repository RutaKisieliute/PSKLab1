package med.usecases;

import lombok.Getter;
import lombok.Setter;
import med.entities.Doctor;
import med.entities.Patient;
import med.entities.Visit;
import med.services.DoctorService;
import med.services.PatientService;
import med.services.VisitService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class VisitUsecase implements Serializable {

    @Inject
    private VisitService visitService;

    @Inject
    private DoctorService doctorService;

    @Inject
    private PatientService patientService;

    @Getter @Setter
    private Visit newVisit = new Visit();

    @Getter @Setter
    private Integer selectedDoctorId;

    @Getter @Setter
    private Integer selectedPatientId;

    @Getter
    private List<Doctor> doctors;

    @Getter
    private List<Patient> patients;

    @PostConstruct
    public void init() {
        doctors = doctorService.getAllDoctors();
        patients = patientService.getAllPatients();
    }

    public List<Visit> getAllVisits() {
        return visitService.getAllVisits();
    }

    public String createVisit() {
        if (selectedDoctorId == null || selectedPatientId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Doctor and patient must be selected", ""));
            return null;
        }

        Doctor doctor = doctorService.findById(selectedDoctorId);
        Patient patient = patientService.findById(selectedPatientId);

        if (doctor == null || patient == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid doctor or patient selection", ""));
            return null;
        }

        newVisit.setDoctor(doctor);
        newVisit.setPatient(patient);

        if (visitService.createVisit(newVisit)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Visit created successfully", ""));
            newVisit = new Visit(); // Reset form
            return "visitList?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating visit", ""));
        return null;
    }
}
