
public class HospitalOrder extends LabOrder {

    public HospitalOrder(LabTest labTest, Patient patient, Doctor doctor) {
        super(labTest, patient, doctor, 500.0);
    }

    @Override
    public String info() {
        double price = calculateContribution();
        return String.format(
            "A %s (Code# %s) is ordered within the hospital for %s that costs %.2f TRY",
            getLabTestName(),
            getLabCode(),
            patient.getPatientName(),
            price
        );
    }
}

