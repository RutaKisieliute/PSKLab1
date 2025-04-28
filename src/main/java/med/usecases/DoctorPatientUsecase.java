package med.usecases;

import lombok.Getter;
import lombok.Setter;
import med.entities.Doctor;
import med.entities.Patient;
import med.services.DoctorService;
import med.services.PatientService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class DoctorPatientUsecase implements Serializable {

    @Inject
    private DoctorService doctorService;

    @Inject
    private PatientService patientService;

    @Getter
    private List<Doctor> doctors;

    @Getter
    private List<Patient> patients;

    @Getter
    private List<Patient> assignedPatients = new ArrayList<>();

    @Getter @Setter
    private Integer selectedDoctorId;

    @Getter @Setter
    private Integer selectedPatientId;

    @PostConstruct
    public void init() {
        doctors = doctorService.getAllDoctors();
        patients = patientService.getAllPatients();
    }

    public void onDoctorChange() {
        loadAssignedPatients();
    }

    private void loadAssignedPatients() {
        assignedPatients.clear();
        if (selectedDoctorId != null) {
            Doctor doctor = doctorService.findById(selectedDoctorId);
            if (doctor != null && doctor.getPatients() != null) {
                assignedPatients.addAll(doctor.getPatients());
            }
        }
    }

    public boolean isDoctorAssignedToPatient() {
        if (selectedDoctorId != null && selectedPatientId != null) {
            Doctor doctor = doctorService.findById(selectedDoctorId);
            Patient patient = patientService.findById(selectedPatientId);
            return doctor != null && patient != null && patient.getDoctors().contains(doctor);
        }
        return false;
    }

    @Transactional
    public String assignDoctorToPatient() {
        if (selectedDoctorId != null && selectedPatientId != null) {
            Doctor doctor = doctorService.findById(selectedDoctorId);
            Patient patient = patientService.findById(selectedPatientId);

            if (doctor != null && patient != null) {
                if (!patient.getDoctors().contains(doctor)) {
                    patient.getDoctors().add(doctor);
                    doctor.getPatients().add(patient);

                    patientService.updatePatient(patient);
                    doctorService.updateDoctor(doctor);

                    loadAssignedPatients();
                    selectedPatientId = null;
                    return null;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Doctor already assigned to this patient.", ""));
                }
            }
        }
        return null;
    }
}
