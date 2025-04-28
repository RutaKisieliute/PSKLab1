package med.usecasesBatis;

import lombok.Getter;
import lombok.Setter;
import med.mybatis.model.Doctor;
import med.mybatis.model.Patient;
import med.mybatis.model.Visit;
import med.servicesBatis.BatisDoctorService;
import med.servicesBatis.BatisPatientService;
import med.servicesBatis.BatisVisitService;

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
public class BatisVisitUsecase implements Serializable {

    @Inject
    private BatisVisitService visitService;

    @Inject
    private BatisDoctorService doctorService;

    @Inject
    private BatisPatientService patientService;

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
        if (doctorService == null || patientService == null) {
            System.out.println("doctorService or patientService is null in @PostConstruct");
            return;
        }

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

        newVisit.setDoctorId(doctor.getId());
        newVisit.setPatientId(patient.getId());

        if (visitService.createVisit(newVisit)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Visit created successfully", ""));
            newVisit = new Visit(); // Reset form
            return "batisVisitList?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating visit", ""));
        return null;
    }
}
