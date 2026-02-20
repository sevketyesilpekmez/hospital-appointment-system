
public class Patient {
    private String patientName;
    private int patientId;

    public Patient(String patientName, int patientId) {
        this.patientName = patientName;
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String toString() {
        return patientName + " (ID: " + patientId + ")";
    }
}
