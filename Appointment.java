
public class Appointment {

    private Doctor doctor;
    private Patient patient;
    private MyDate date;

    public Appointment(Doctor doctor, Patient patient, MyDate date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MyDate getDate() {
        return date;
    }

    public void setDate(MyDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return date.toString() + " - Patient: " + patient.getPatientName()
                + " | Doctor: " + doctor.getDoctorName();
    }
}
