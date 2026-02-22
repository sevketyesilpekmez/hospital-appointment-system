
public class MedicareOrder extends LabOrder {

    public MedicareOrder(LabTest labTest, Patient patient, Doctor doctor) {
        super(labTest, patient, doctor, 1000.0);
    }

    @Override
    public double calculateContribution() {
        return baseCost * 0.8;
    }

    @Override
    public String info() {
        double original = baseCost;
        double discounted = calculateContribution();

        return String.format(
            "A %s (Code# %s) is ordered via the Medicare System for %s " +
            "that costs %.2f TRY which is 20%% less from the original %.2f TRY price " +
            "due to end of year discount",
            getLabTestName(),
            getLabCode(),
            patient.getPatientName(),
            discounted,
            original
        );
    }
}
