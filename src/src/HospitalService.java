import java.util.ArrayList;
import java.util.Collections;

public class HospitalService {

    private final ArrayList<Doctor> doctors = new ArrayList<>();
    private final ArrayList<Billable> hospitalDatabase = new ArrayList<>();
    private final ArrayList<Patient> patients = new ArrayList<>();
    private final ArrayList<LabTest> labCatalog = new ArrayList<>();

    // -------------------- ADD DOCTOR / PATIENT / LAB TEST --------------------

    public void addDoctor(String typeText, String name, String dept, int id) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty.");
        }
        if (dept == null || dept.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Doctor ID must be a positive integer.");
        }
        if (findDoctor(id) != null) {
            throw new IllegalArgumentException("A doctor with this ID already exists.");
        }

        Doctor d;
        String t = (typeText == null) ? "" : typeText.trim().toLowerCase();
        if (t.equals("general practitioner")) {
            d = new GeneralPractitioner(name.trim(), dept.trim(), id);
        } else if (t.equals("surgeon")) {
            d = new Surgeon(name.trim(), dept.trim(), id);
        } else if (t.equals("specialist")) {
            d = new Specialist(name.trim(), dept.trim(), id);
        } else {
            throw new IllegalArgumentException("Invalid doctor type.");
        }

        doctors.add(d);
        hospitalDatabase.add(d);
    }

    public void addPatient(String name, int id) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Patient ID must be a positive integer.");
        }
        if (findPatient(id) != null) {
            throw new IllegalArgumentException("A patient with this ID already exists.");
        }
        patients.add(new Patient(name.trim(), id));
    }

    public void addLabTest(String testName, String testCode) {
        if (testName == null || testName.trim().isEmpty()) {
            throw new IllegalArgumentException("Lab test name cannot be empty.");
        }
        if (testCode == null || testCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Lab test code cannot be empty.");
        }
        if (findLabInCatalog(testCode.trim()) != null) {
            throw new IllegalArgumentException("A lab test with this code already exists.");
        }
        labCatalog.add(new LabTest(testName.trim(), testCode.trim()));
    }

    // -------------------- OPERATIONS --------------------

    public void bookAppointment(int patientId, int doctorId, String dateStr) throws NotValidDateException {
        Patient p = findPatient(patientId);
        if (p == null) throw new IllegalArgumentException("There is no patient with that ID.");

        Doctor d = findDoctor(doctorId);
        if (d == null) throw new IllegalArgumentException("There is no doctor with that ID.");

        MyDate date = parseAndValidateDate(dateStr); // throws NotValidDateException
        Appointment appt = new Appointment(d, p, date);

        if (!d.addAppointment(appt)) {
            throw new IllegalArgumentException("Appointment could not be booked due to limit.");
        }
    }

    public void orderLabTest(int patientId, int doctorId, String testCode, String provider)
            throws NotValidProviderException {
        Patient p = findPatient(patientId);
        if (p == null) throw new IllegalArgumentException("There is no patient with that ID.");

        Doctor d = findDoctor(doctorId);
        if (d == null) throw new IllegalArgumentException("There is no doctor with that ID.");

        if (testCode == null || testCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Test code cannot be empty.");
        }
        LabTest catalog = findLabInCatalog(testCode.trim());
        if (catalog == null) {
            throw new IllegalArgumentException("There is no lab test with that code in the catalog.");
        }

        validateProvider(provider);

        LabOrder ordered;
        String pType = provider.trim().toLowerCase();
        if (pType.equals("hospital")) {
            ordered = new HospitalOrder(catalog, p, d);
        } else if (pType.equals("medicare")) {
            ordered = new MedicareOrder(catalog, p, d);
        } else {
            ordered = new ExternalOrder(catalog, p, d);
        }

        if (d.addLab(ordered)) {
            hospitalDatabase.add(ordered);
        } else {
            throw new IllegalArgumentException("The lab test could not be ordered due to limit.");
        }
    }

    // -------------------- REPORTS --------------------

    public String buildDisplayAllText() {
        StringBuilder sb = new StringBuilder();

        sb.append("--- Doctors ---\n");
        for (Doctor d : doctors) {
            sb.append("Doctor: ")
              .append(d.getDoctorName())
              .append(" (").append(d.getDepartment()).append(")")
              .append(" [Type: ").append(d.getDoctorType()).append("]\n");

            sb.append("Booked Appointments:\n");
            if (d.getBookedAppointmentCount() == 0) {
                sb.append(" - (none)\n");
            } else {
                for (Appointment a : d.getBookedAppointments()) {
                    sb.append(" - ")
                      .append(a.getPatient().getPatientName())
                      .append(" - ").append(a.getDate())
                      .append("\n");
                }
            }

            sb.append("Ordered Lab Tests:\n");
            if (d.getOrderedLabTestCount() == 0) {
                sb.append(" - (none)\n");
            } else {
                for (LabOrder o : d.getOrderedLabTests()) {
                    sb.append(o.info()).append("\n");
                }
            }

            sb.append("Doctor's Activity Summary:\n");
            if (d.getBookedAppointmentCount() == 0) {
                sb.append(" - (none)\n");
            } else {
                sb.append(String.format(
                        "Doctor with id number %d named %s from department %s will contribute %.2f TRY%n",
                        d.getDoctorId(), d.getDoctorName(), d.getDepartment(), d.calculateCost()
                ));
            }
            sb.append("\n");
        }

        sb.append("--- Patients ---\n");
        for (Patient p : patients) {
            sb.append(p.getPatientName())
              .append(" (ID: ").append(p.getPatientId()).append(")\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    public String buildSortedFinancialReportText() {
        StringBuilder sb = new StringBuilder();

        ArrayList<Doctor> doctorList = new ArrayList<>();
        for (Billable b : hospitalDatabase) {
            if (b instanceof Doctor) doctorList.add((Doctor) b);
        }
        Collections.sort(doctorList); // Doctor implements Comparable (descending contribution in your setup)

        sb.append("[Doctors] Sorted by Contribution (Descending):\n");
        int rank = 1;
        for (Doctor d : doctorList) {
            sb.append(String.format("%d. %s (%s) - %.2f TRY%n",
                    rank++,
                    d.getDoctorName(),
                    d.getDoctorType(),
                    d.calculateCost()));
        }

        sb.append("\n");

        ArrayList<LabTest> tests = new ArrayList<>(labCatalog);
        LabTestRevenueComparator comp = new LabTestRevenueComparator(hospitalDatabase);
        Collections.sort(tests, comp); // ascending revenue via comparator

        sb.append("[Lab Tests] Sorted by Total Revenue (Ascending):\n");
        rank = 1;
        for (LabTest t : tests) {
            sb.append(String.format("%d. %s (%s): Total Generated: %.2f TRY%n",
                    rank++,
                    t.getLabTestName(),
                    t.getLabCode(),
                    comp.totalRevenueOf(t)));
        }

        sb.append("\n");
        return sb.toString();
    }

    // -------------------- HELPERS --------------------

    private Doctor findDoctor(int id) {
        for (Doctor d : doctors) {
            if (d.getDoctorId() == id) return d;
        }
        return null;
    }

    private Patient findPatient(int id) {
        for (Patient p : patients) {
            if (p.getPatientId() == id) return p;
        }
        return null;
    }

    private LabTest findLabInCatalog(String code) {
        for (LabTest lt : labCatalog) {
            if (lt.getLabCode().equalsIgnoreCase(code)) return lt;
        }
        return null;
    }

    private MyDate parseAndValidateDate(String dateStr) throws NotValidDateException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new NotValidDateException("Invalid date: empty");
        }
        String s = dateStr.trim();

        // Accept: DD/MM/YYYY or DD-MM-YYYY
        String[] parts = s.split("[/\\-]");
        if (parts.length != 3) {
            throw new NotValidDateException("Invalid date format. Use DD/MM/YYYY");
        }

        int day, month, year;
        try {
            day = Integer.parseInt(parts[0].trim());
            month = Integer.parseInt(parts[1].trim());
            year = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new NotValidDateException("Invalid date: " + s);
        }

        validateDate(day, month, year);
        return new MyDate(day, month, year);
    }

    private void validateDate(int day, int month, int year) throws NotValidDateException {
        if (month < 1 || month > 12) {
            throw new NotValidDateException("Invalid date: " + day + "/" + month + "/" + year);
        }
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 2 && isLeapYear(year)) {
            daysInMonth[1] = 29;
        }
        if (day < 1 || day > daysInMonth[month - 1]) {
            throw new NotValidDateException("Invalid date: " + day + "/" + month + "/" + year);
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void validateProvider(String provider) throws NotValidProviderException {
        if (provider == null) {
            throw new NotValidProviderException("Invalid provider: null");
        }
        String p = provider.trim().toLowerCase();
        if (!p.equals("medicare") && !p.equals("hospital") && !p.equals("external")) {
            throw new NotValidProviderException("Invalid provider: " + provider);
        }
    }
}
