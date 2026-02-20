import javax.swing.*;
import java.awt.*;

public class HospitalFrame extends JFrame {
    private final HospitalService service;
    private JTextArea outputArea;

    public HospitalFrame(HospitalService service) {
        this.service = service;

        setTitle("Hospital Management System - Assignment 9");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Registration", buildRegistrationPanel());
        tabs.add("Operations", buildOperationsPanel());
        tabs.add("Reports & Stats", buildReportsPanel());

        add(tabs);
    }

    private JPanel buildRegistrationPanel() {
        JPanel p = new JPanel(new GridLayout(1, 3, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        p.add(buildAddDoctorPanel());
        p.add(buildAddPatientPanel());
        p.add(buildAddLabTestPanel());

        return p;
    }

    private JPanel buildOperationsPanel() {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        p.add(buildBookAppointmentPanel());
        p.add(buildOrderLabTestPanel());

        return p;
    }

    private JPanel buildReportsPanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnDisplay = new JButton("Display All Accounts");
        JButton btnSorted = new JButton("Generate Financial Report (Sorted)");
        top.add(btnDisplay);
        top.add(btnSorted);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        btnDisplay.addActionListener(e -> outputArea.setText(service.buildDisplayAllText()));
        btnSorted.addActionListener(e -> outputArea.setText(service.buildSortedFinancialReportText()));

        root.add(top, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    // ---------------- Registration sub-panels ----------------

    private JPanel buildAddDoctorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Doctor"));
        GridBagConstraints c = gbc();

        JTextField tfName = new JTextField(14);
        JTextField tfDept = new JTextField(14);
        JTextField tfId = new JTextField(14);
        JComboBox<String> cbType = new JComboBox<>(new String[]{"General Practitioner", "Surgeon", "Specialist"});
        JButton btnAdd = new JButton("Add Doctor");

        addRow(panel, c, 0, "Name:", tfName);
        addRow(panel, c, 1, "Department:", tfDept);
        addRow(panel, c, 2, "ID:", tfId);
        addRow(panel, c, 3, "Type:", cbType);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnAdd, c);

        btnAdd.addActionListener(e -> {
            try {
                String name = tfName.getText();
                String dept = tfDept.getText();
                int id = Integer.parseInt(tfId.getText().trim());
                String type = (String) cbType.getSelectedItem();

                service.addDoctor(type, name, dept, id);
                showMessage("Doctor added successfully!");
                tfName.setText(""); tfDept.setText(""); tfId.setText("");
            } catch (NumberFormatException ex) {
                // Fig. 5 formatına yakın olsun diye "Invalid..." kullanmıyoruz; burada ID alanı doctor paneli
                showMessage("Invalid Doctor ID.");
            } catch (Exception ex) {
                // Ödev görsellerinde error yerine message var
                showMessage(ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildAddPatientPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));
        GridBagConstraints c = gbc();

        JTextField tfName = new JTextField(14);
        JTextField tfId = new JTextField(14);
        JButton btnAdd = new JButton("Add Patient");

        addRow(panel, c, 0, "Name:", tfName);
        addRow(panel, c, 1, "ID:", tfId);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnAdd, c);

        btnAdd.addActionListener(e -> {
            try {
                String name = tfName.getText();
                int id = Integer.parseInt(tfId.getText().trim());

                service.addPatient(name, id);
                showMessage("Patient added successfully!");
                tfName.setText(""); tfId.setText("");
            } catch (NumberFormatException ex) {
                showMessage("Invalid Patient ID.");
            } catch (Exception ex) {
                showMessage(ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildAddLabTestPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add Lab Test"));
        GridBagConstraints c = gbc();

        JTextField tfName = new JTextField(14);
        JTextField tfCode = new JTextField(14);
        JButton btnAdd = new JButton("Add Lab Test");

        addRow(panel, c, 0, "Test Name:", tfName);
        addRow(panel, c, 1, "Test Code:", tfCode);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnAdd, c);

        btnAdd.addActionListener(e -> {
            try {
                service.addLabTest(tfName.getText(), tfCode.getText());
                showMessage("Lab test added successfully!");
                tfName.setText(""); tfCode.setText("");
            } catch (Exception ex) {
                showMessage(ex.getMessage());
            }
        });

        return panel;
    }

    // ---------------- Operations sub-panels ----------------

    private JPanel buildBookAppointmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Book Appointment"));
        GridBagConstraints c = gbc();

        JTextField tfPid = new JTextField(14);
        JTextField tfDid = new JTextField(14);
        JTextField tfDate = new JTextField(14); // DD/MM/YYYY
        JButton btnBook = new JButton("Book Appointment");

        addRow(panel, c, 0, "Patient ID:", tfPid);
        addRow(panel, c, 1, "Doctor ID:", tfDid);
        addRow(panel, c, 2, "Date (DD/MM/YYYY):", tfDate);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnBook, c);

        btnBook.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(tfPid.getText().trim());
                int did = Integer.parseInt(tfDid.getText().trim());

                service.bookAppointment(pid, did, tfDate.getText());
                showMessage("Appointment booked successfully!");

                tfPid.setText(""); tfDid.setText(""); tfDate.setText("");
            } catch (NumberFormatException ex) {
                // Fig. 5 ile birebir
                showInvalidIdMessage();
            } catch (IllegalArgumentException ex) {
                // patient/doctor yoksa veya id ile ilgiliyse Fig.5'e çevir
                String m = (ex.getMessage() == null) ? "" : ex.getMessage().toLowerCase();
                if (m.contains("patient") || m.contains("doctor") || m.contains("id")) {
                    showInvalidIdMessage();
                } else {
                    showMessage(ex.getMessage());
                }
            } catch (Exception ex) {
                showMessage(ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildOrderLabTestPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Order Lab Test"));
        GridBagConstraints c = gbc();

        JTextField tfPid = new JTextField(14);
        JTextField tfDid = new JTextField(14);
        JTextField tfCode = new JTextField(14);
        JComboBox<String> cbProvider = new JComboBox<>(new String[]{"MediCare", "Hospital", "External"});
        JButton btnOrder = new JButton("Place Order");

        addRow(panel, c, 0, "Patient ID:", tfPid);
        addRow(panel, c, 1, "Doctor ID:", tfDid);
        addRow(panel, c, 2, "Test Code:", tfCode);
        addRow(panel, c, 3, "Provider:", cbProvider);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnOrder, c);

        btnOrder.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(tfPid.getText().trim());
                int did = Integer.parseInt(tfDid.getText().trim());
                String provider = (String) cbProvider.getSelectedItem();

                service.orderLabTest(pid, did, tfCode.getText(), provider);
                showMessage("Lab test ordered successfully!");

                tfPid.setText(""); tfDid.setText(""); tfCode.setText("");
            } catch (NumberFormatException ex) {
                // Fig. 5 ile birebir
                showInvalidIdMessage();
            } catch (IllegalArgumentException ex) {
                String m = (ex.getMessage() == null) ? "" : ex.getMessage().toLowerCase();
                if (m.contains("patient") || m.contains("doctor") || m.contains("id")) {
                    showInvalidIdMessage();
                } else {
                    showMessage(ex.getMessage());
                }
            } catch (Exception ex) {
                showMessage(ex.getMessage());
            }
        });

        return panel;
    }

    // ---------------- Layout helpers ----------------

    private GridBagConstraints gbc() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        return c;
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridy = row;

        c.gridx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.gridwidth = 1;
        panel.add(field, c);
    }

    // ---------------- Message helpers (PDF ile aynı görünüm) ----------------

    private void showMessage(String text) {
        JOptionPane.showMessageDialog(this, text, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInvalidIdMessage() {
        showMessage("Invalid Patient or Doctor ID.");
    }
}
