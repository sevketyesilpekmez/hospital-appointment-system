
public class Surgeon extends Doctor {

    private static final double CONTRIBUTION_PER_APPOINTMENT = 5000.0;

    public Surgeon(String doctorName, String department, int doctorId) {
        super(doctorName, department, doctorId);
    }

    @Override
    protected int getMaxAppointmentsPerPatient() {
        return 3;
    }

    @Override
    protected int getMaxLabsPerPatient() {
        return 3;
    }

    @Override
    public String getDoctorType() {
        return "Surgeon";
    }

    @Override
    public double calculateContribution() {
        return getBookedAppointmentCount() * CONTRIBUTION_PER_APPOINTMENT;
    }
    @Override
    public String info() {
        return String.format(
            "Doctor with id number %d named %s from department %s will contribute %.2f TRY",
            getDoctorId(),
            getDoctorName(),
            getDepartment(),
            calculateContribution()
        );
    }

}

