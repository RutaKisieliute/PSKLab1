package med.usecasesBatis;

import lombok.Getter;
import lombok.Setter;
import med.mybatis.dao.DoctorMapper;
import med.mybatis.dao.PatientDoctorMapper;
import med.mybatis.dao.PatientMapper;
import med.mybatis.model.Doctor;
import med.mybatis.model.Patient;
import med.mybatis.model.PatientDoctor;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class BatisDoctorPatientUsecase implements Serializable {

    @Inject
    private DoctorMapper doctorMapper;

    @Inject
    private PatientMapper patientMapper;

    @Inject
    private PatientDoctorMapper patientDoctorMapper;

    @Getter
    private List<Doctor> doctors;

    @Getter
    private List<Patient> patients;

    @Getter
    private List<Patient> assignedPatients = new ArrayList<>();
    @Getter
    private Map<Integer, List<Doctor>> patientDoctorMap = new HashMap<>();

    @Getter @Setter
    private Integer selectedDoctorId;

    @Getter @Setter
    private Integer selectedPatientId;

    @PostConstruct
    public void init() {
        doctors = doctorMapper.selectAll();
        patients = patientMapper.selectAll();
    }

    public void onDoctorChange() {
        loadAssignedPatients();
    }

    public void loadAssignedPatients() {
        assignedPatients.clear();
        patientDoctorMap.clear();

        if (selectedDoctorId != null) {
            List<PatientDoctor> mappings = patientDoctorMapper.selectAll();
            List<Integer> patientIds = mappings.stream()
                    .filter(pd -> pd.getDoctorId().equals(selectedDoctorId))
                    .map(PatientDoctor::getPatientId)
                    .collect(Collectors.toList());

            for (Integer patientId : patientIds) {
                Patient patient = patientMapper.selectByPrimaryKey(patientId);
                if (patient != null) {
                    assignedPatients.add(patient);
                    // Load doctors for this patient
                    List<Doctor> doctorsForPatient = getDoctorsForPatient(patientId);
                    patientDoctorMap.put(patientId, doctorsForPatient);
                }
            }
        }
    }


    public boolean isDoctorAssignedToPatient() {
        if (selectedDoctorId != null && selectedPatientId != null) {
            List<PatientDoctor> mappings = patientDoctorMapper.selectAll();
            return mappings.stream()
                    .anyMatch(pd -> pd.getDoctorId().equals(selectedDoctorId) && pd.getPatientId().equals(selectedPatientId));
        }
        return false;
    }

    @Transactional
    public String assignDoctorToPatient() {
        if (selectedDoctorId != null && selectedPatientId != null) {
            if (!isDoctorAssignedToPatient()) {
                PatientDoctor pd = new PatientDoctor();
                pd.setDoctorId(selectedDoctorId);
                pd.setPatientId(selectedPatientId);
                patientDoctorMapper.insert(pd);

                loadAssignedPatients(); // Refresh list
                selectedPatientId = null;
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Doctor already assigned to this patient.", ""));
            }
        }
        return null;
    }

    public List<Doctor> getDoctorsForPatient(Integer patientId) {
        List<Doctor> result = new ArrayList<>();
        List<PatientDoctor> mappings = patientDoctorMapper.selectAll();

        for (PatientDoctor pd : mappings) {
            if (pd.getPatientId().equals(patientId)) {
                Doctor doctor = doctorMapper.selectByPrimaryKey(pd.getDoctorId());
                if (doctor != null) {
                    result.add(doctor);
                }
            }
        }

        return result;
    }
}
