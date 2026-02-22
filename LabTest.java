
public class LabTest {
    private String labTestName;
    private String labCode;
    private Patient patient;
    private Doctor doctor;


    public LabTest(String labTestName, String code) {
        this.labTestName = labTestName;
        this.labCode = code;
        this.patient = null;
        this.doctor = null;
    }

    public LabTest(String labTestName, String labCode, Patient patient, Doctor doctor) {
        this.labTestName = labTestName;
        this.labCode = labCode;
        this.patient = patient;
        this.doctor = doctor;
    }

    public String getLabTestName() {
        return labTestName;
    }

    public void setLabTestName(String labTestName) {
        this.labTestName = labTestName;
    }

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        if (patient == null || doctor == null) {
            return labTestName + " (Code: " + labCode + ")";
        }
        return labTestName + " (Code: " + labCode + ") — Patient: "
                + patient.getPatientName() + ", Doctor: " + doctor.getDoctorName();
    }
}
