
import java.util.Comparator;
import java.util.List;


public class LabTestRevenueComparator implements Comparator<LabTest> {

    private final List<Billable> hospitalDatabase;

    public LabTestRevenueComparator(List<Billable> hospitalDatabase) {
        this.hospitalDatabase = hospitalDatabase;
    }

    public double totalRevenueOf(LabTest test) {
        double total = 0.0;
        for (Billable b : hospitalDatabase) {
            if (b instanceof LabOrder) {
                LabOrder o = (LabOrder) b;
                if (o.getLabCode().equalsIgnoreCase(test.getLabCode())) {
                    total += o.calculateCost();
                }
            }
        }
        return total;
    }

    @Override
    public int compare(LabTest t1, LabTest t2) {
        return Double.compare(totalRevenueOf(t1), totalRevenueOf(t2));
    }
}
