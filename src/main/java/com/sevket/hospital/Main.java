
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            HospitalService service = new HospitalService();
            HospitalFrame frame = new HospitalFrame(service);
            frame.setVisible(true);
        });
    }
}
