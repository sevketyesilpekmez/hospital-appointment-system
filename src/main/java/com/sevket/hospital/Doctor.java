
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Doctor implements Billable, Comparable<Doctor> {

    private String doctorName;
    private String department;
    private int doctorId;

    private ArrayList<Appointment> bookedAppointments;
    private ArrayList<LabOrder> orderedLabTests;

    private static int doctorCount = 0;

    public Doctor(String doctorName, String department, int doctorId) {
        this.doctorName = doctorName;
        this.department = department;
        this.doctorId = doctorId;

        bookedAppointments = new ArrayList<>();
        orderedLabTests = new ArrayList<>();

        doctorCount++;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDepartment() {
        return department;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public abstract String getDoctorType();

    protected abstract int getMaxAppointmentsPerPatient();
    protected abstract int getMaxLabsPerPatient();

    public boolean addAppointment(Appointment appt) {
        int count = 0;
        for (Appointment a : bookedAppointments) {
            if (a.getPatient().getPatientId() == appt.getPatient().getPatientId()) {
                count++;
            }
        }
        if (count >= getMaxAppointmentsPerPatient()) return false;

        bookedAppointments.add(appt);
        return true;
    }

    public boolean cancelAppointment(String patientId, MyDate date) {
        Iterator<Appointment> it = bookedAppointments.iterator();
        while (it.hasNext()) {
            Appointment a = it.next();
            if (String.valueOf(a.getPatient().getPatientId()).equals(patientId) &&
                    a.getDate().equals(date)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean addLab(LabOrder order) {
        int count = 0;
        for (LabOrder lo : orderedLabTests) {
            if (lo.getPatient().getPatientId() == order.getPatient().getPatientId()) {
                count++;
            }
        }
        if (count >= getMaxLabsPerPatient()) return false;

        orderedLabTests.add(order);
        return true;
    }

    public boolean cancelLabOrder(String patientId, String labCode) {
        Iterator<LabOrder> it = orderedLabTests.iterator();
        while (it.hasNext()) {
            LabOrder o = it.next();
            if (String.valueOf(o.getPatient().getPatientId()).equals(patientId)
                    && o.getLabCode().equalsIgnoreCase(labCode)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Appointment[] getBookedAppointments() {
        return bookedAppointments.toArray(new Appointment[0]);
    }

    public int getBookedAppointmentCount() {
        return bookedAppointments.size();
    }

    public LabOrder[] getOrderedLabTests() {
        return orderedLabTests.toArray(new LabOrder[0]);
    }

    public int getOrderedLabTestCount() {
        return orderedLabTests.size();
    }

    @Override
    public int compareTo(Doctor other) {
        return Double.compare(other.calculateCost(), this.calculateCost());
    }

    public static int getDoctorCount() {
        return doctorCount;
    }
}
