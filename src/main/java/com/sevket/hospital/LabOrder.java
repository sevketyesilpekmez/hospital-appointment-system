
public abstract class LabOrder implements Billable {

    protected LabTest labTest;
    protected Patient patient;
    protected Doctor doctor;
    protected double baseCost;   

    public LabOrder(LabTest labTest, Patient patient, Doctor doctor, double baseCost) {
        this.labTest = labTest;
        this.patient = patient;
        this.doctor = doctor;
        this.baseCost = baseCost;
    }

    public LabTest getLabTest() {
        return labTest;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getLabCode() {
        return labTest.getLabCode();
    }

    public String getLabTestName() {
        return labTest.getLabTestName();
    }

    public double getBaseCost() {
        return baseCost;
    }

    @Override
    public double calculateContribution() {
        return baseCost;
    }
}
