
public class Specialist extends Doctor {

    private static final double CONTRIBUTION_PER_APPOINTMENT = 2000.0;

    public Specialist(String doctorName, String department, int doctorId) {
        super(doctorName, department, doctorId);
    }

    @Override
    protected int getMaxAppointmentsPerPatient() {
        return 2;
    }

    @Override
    protected int getMaxLabsPerPatient() {
        return 2;
    }

    @Override
    public String getDoctorType() {
        return "Specialist";
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
