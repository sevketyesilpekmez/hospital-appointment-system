
public  class GeneralPractitioner extends Doctor {

    private static final double CONTRIBUTION_PER_APPOINTMENT = 1000.0;

    public GeneralPractitioner(String doctorName, String department, int doctorId) {
        super(doctorName, department, doctorId);
    }

    @Override
    protected int getMaxAppointmentsPerPatient() {
        return 1;
    }

    @Override
    protected int getMaxLabsPerPatient() {
        return 1;
    }

    @Override
    public String getDoctorType() {
        return "General Practitioner";
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

