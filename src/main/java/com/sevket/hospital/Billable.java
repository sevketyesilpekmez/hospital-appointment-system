
public interface Billable {
    double calculateContribution();
    String info();

        default double calculateCost() {
        return calculateContribution();
    }
}
