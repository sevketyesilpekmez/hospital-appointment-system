
public class ExternalOrder extends LabOrder {

    public ExternalOrder(LabTest labTest, Patient patient, Doctor doctor) {
        super(labTest, patient, doctor, 10000.0);
    }

    @Override
    public String info() {
        double price = calculateContribution();
        return String.format(
            "A %s (Code# %s) is ordered from an external laboratory for %s that costs %.2f TRY",
            getLabTestName(),
            getLabCode(),
            patient.getPatientName(),
            price
        );
    }
}
